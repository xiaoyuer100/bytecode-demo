package org.codewind.agent.asm.adpter;

/**
 * @author xuansu
 * @since 2020/6/20 7:43 下午
 */

import org.objectweb.asm.*;
import org.objectweb.asm.commons.AnalyzerAdapter;
import org.objectweb.asm.commons.LocalVariablesSorter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public class TimeCountAdpter extends ClassVisitor implements Opcodes {

    private String owner;
    private boolean isInterface;

    public TimeCountAdpter(ClassVisitor classVisitor) {
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

        if (!isInterface && mv != null && !name.equals("<init>") && name.equals("sayHello")) {
            AddTimerMethodAdapter at = new AddTimerMethodAdapter(mv, owner, name);
            at.analyzerAdapter = new AnalyzerAdapter(owner, access, name, descriptor, at);
            at.localVariablesSorter = new LocalVariablesSorter(access, descriptor, at.analyzerAdapter);

            return at.localVariablesSorter;
        }

        return mv;
    }

    public void visitEnd() {
        cv.visitEnd();
    }

    class AddTimerMethodAdapter extends MethodVisitor {
        private int time;
        private int maxStack;
        private String methodName;
        public LocalVariablesSorter localVariablesSorter;
        public AnalyzerAdapter analyzerAdapter;

        public AddTimerMethodAdapter(MethodVisitor methodVisitor, String owner, String methodName) {
            super(ASM8, methodVisitor);
            this.methodName = methodName;
        }


        @Override
        public void visitCode() {
            mv.visitCode();
            //startTime = System.nanoTime();
            mv.visitMethodInsn(INVOKESTATIC, "java/lang/System", "nanoTime", "()J", isInterface);
            time = localVariablesSorter.newLocal(Type.LONG_TYPE);
            mv.visitVarInsn(LSTORE, time);
            maxStack = 4;
        }

        @Override
        public void visitInsn(int opcode) {
            if ((opcode >= IRETURN && opcode <= RETURN) || opcode == ATHROW) {
                //endTime = System.nanoTime() - var1;
                mv.visitMethodInsn(INVOKESTATIC, "java/lang/System", "nanoTime", "()J", isInterface);
                mv.visitVarInsn(LLOAD, time);
                mv.visitInsn(LSUB);
                mv.visitVarInsn(LSTORE, time);

                // 输出方法总耗时
//                mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
//                mv.visitLdcInsn(owner+ "#" + methodName + " all time consume:");
//                mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "print", "(Ljava/lang/String;)V", isInterface);
//
//                //System.out.println(method=%s cost time);
//                mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
//                mv.visitVarInsn(LLOAD, time);
//                mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(J)V", isInterface);

                // 计算方法耗时
                mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
                mv.visitLdcInsn(owner);
                mv.visitLdcInsn(methodName);
                mv.visitVarInsn(LLOAD, time);
                mv.visitMethodInsn(INVOKESTATIC, "util/TimeUtil", "traceMethodCostTime", "(Ljava/lang/String;Ljava/lang/String;J)Ljava/lang/String;", false);
                mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);

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