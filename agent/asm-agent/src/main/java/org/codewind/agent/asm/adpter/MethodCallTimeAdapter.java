package org.codewind.agent.asm.adpter;

/**
 * 方法调用耗时适配器
 *
 * @author xuansu
 * @since 2020/6/20 7:43 下午
 */

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.AnalyzerAdapter;
import org.objectweb.asm.commons.LocalVariablesSorter;

public class MethodCallTimeAdapter extends ClassVisitor implements Opcodes {

    private String owner;
    private boolean isInterface;

    public MethodCallTimeAdapter(ClassVisitor classVisitor) {
        super(ASM8, classVisitor);
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        super.visit(version, access, name, signature, superName, interfaces);
        owner = name;
        isInterface = (access & ACC_INTERFACE) != 0;
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        MethodVisitor mv = cv.visitMethod(access, name, descriptor, signature, exceptions);
        if (!isInterface && mv != null && !name.equals("<init>")) {
            AddTimerMethodAdapter at = new AddTimerMethodAdapter(mv, owner, access, name, descriptor);
            return at.localVariablesSorter;
        }
        return mv;
    }

    public void visitEnd() {
        cv.visitEnd();
    }

    class AddTimerMethodAdapter extends MethodVisitor {
        //调用耗时变量地址
        private int time;
        //最大栈大小
        private int maxStack;
        //方法名称
        private String methodName;
        //本地变量帮助类
        public LocalVariablesSorter localVariablesSorter;
        //分析适配器
        public AnalyzerAdapter analyzerAdapter;

        public AddTimerMethodAdapter(MethodVisitor mv, String owner, int access, String name, String descriptor) {
            super(ASM8, mv);
            this.methodName = name;
            this.analyzerAdapter = new AnalyzerAdapter(owner, access, name, descriptor, this);
            this.localVariablesSorter = new LocalVariablesSorter(access, descriptor, this.analyzerAdapter);
        }

        @Override
        public void visitCode() {
            mv.visitCode();

            //startTime = System.nanoTime();
            mv.visitMethodInsn(INVOKESTATIC, "java/lang/System", "nanoTime", "()J", isInterface);
            time = localVariablesSorter.newLocal(Type.LONG_TYPE);
            mv.visitVarInsn(LSTORE, time);

            //初始化stack
            maxStack = 4;
        }

        @Override
        public void visitInsn(int opcode) {
            if ((opcode >= IRETURN && opcode <= RETURN) || opcode == ATHROW) {
                //计算耗时，等价: costTime  = System.nanoTime() - startTime;
                mv.visitMethodInsn(INVOKESTATIC, "java/lang/System", "nanoTime", "()J", isInterface);
                mv.visitVarInsn(LLOAD, time);
                mv.visitInsn(LSUB);
                mv.visitVarInsn(LSTORE, time);

                // 输出方法耗时，等价: System.out.pringln(TimeUtil.formatMethodCallTime(owner, methodName, costTime))
                mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
                mv.visitLdcInsn(owner);
                mv.visitLdcInsn(methodName);
                mv.visitVarInsn(LLOAD, time);
                mv.visitMethodInsn(INVOKESTATIC, "org/codewind/agent/asm/util/TraceUtil", "formatMethodCallTime", "(Ljava/lang/String;Ljava/lang/String;J)Ljava/lang/String;", false);
                mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);

                //调整栈的大小
                maxStack = Math.max(analyzerAdapter.stack.size() + 4, maxStack);
            }
            mv.visitInsn(opcode);
        }

        @Override
        public void visitMaxs(int maxStack, int maxLocals) {
            super.visitMaxs(Math.max(maxStack, this.maxStack), maxLocals);
        }
    }

}