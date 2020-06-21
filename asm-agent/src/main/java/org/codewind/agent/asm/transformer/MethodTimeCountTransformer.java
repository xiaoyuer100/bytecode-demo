package org.codewind.agent.asm.transformer;

import org.codewind.agent.asm.adpter.TimeCountAdpter;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

/**
 * 方法计时器
 *
 * @author xuansu
 * @since 2020/6/20 7:42 下午
 */
public class MethodTimeCountTransformer implements ClassFileTransformer {

    private String agentArgs;

    public MethodTimeCountTransformer(String agentArgs) {
        this.agentArgs = agentArgs;
    }

    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        if (!isNeedTransform(className)) {
            return null;
        }
        return transformClass(className);
    }

    /**
     * 是否需要变换
     *
     * @param className
     * @return
     */
    private boolean isNeedTransform(String className) {
        if (className.endsWith("Task")) {
            return true;
        }
        return false;
    }

    /**
     * 转化class
     *
     * @param className
     * @param classfileBuffer
     * @return
     */
    public byte[] transformClass(String className) {
        ClassWriter cw = null;
        TimeCountAdpter timeCountAdpter = null;

        try {
            cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
            timeCountAdpter = new TimeCountAdpter(cw);
        } catch (Exception e) {
            System.out.println("transformClass" + className + " failed !");
        }
        if (timeCountAdpter == null) {
            return null;
        }

        //get class reader
        ClassReader classReader = null;
        try {
            classReader = new ClassReader(className.replaceAll("/", "."));
        } catch (Exception e) {
            System.out.println(String.format("Faile to get classReader, className={}", className));
            e.printStackTrace();
        }
        if (classReader == null) {
            return null;
        }
//        System.out.println("transformClass " + className + " start 3!");
        //accept
        classReader.accept(timeCountAdpter, ClassReader.EXPAND_FRAMES);
        return cw.toByteArray();
    }

}

