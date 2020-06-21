package org.codewind.agent.asm.transformer;

import org.codewind.agent.asm.adpter.MethodCallTimeAdapter;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.security.ProtectionDomain;
import java.util.Optional;

/**
 * 方法调用耗时转换器
 *
 * @author xuansu
 * @since 2020/6/20 7:42 下午
 */
public class MethodCallTimeTransformer implements ClassFileTransformer {

    private String methodSuffix;

    public MethodCallTimeTransformer(String methodSuffix) {
        this.methodSuffix = methodSuffix;
    }

    /**
     * 转化器
     *
     * @param loader              类加载器
     * @param className           类全名，例如 <code>"java/util/List"</code>.
     * @param classBeingRedefined 类定义(可能已经被转换过)
     * @param protectionDomain    正在定义或重新定义的类的保护域
     * @param classfileBuffer     类文件格式的输入字节缓冲区-不能被修改
     * @return transformed class bytecode
     */
    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) {
        if (isNeedTransform(className)) {
            try {
                return transformClass(className);
            } catch (Throwable e) {
                //TODO:此处可替换成log
                System.err.println(String.format("Failed to transform className=%s, error message is = %s",
                        className, Optional.ofNullable(e.getMessage()).orElse("null")));
            }
        }
        return null;
    }

    /**
     * 是否需要变换
     *
     * @param className
     * @return
     */
    private boolean isNeedTransform(String className) {
        if (className.endsWith(methodSuffix)) {
            return true;
        }
        return false;
    }

    /**
     * 转化class
     *
     * @param className
     * @return
     */
    public byte[] transformClass(String className) throws IOException {
        //cw init
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        //time count adpter init
        MethodCallTimeAdapter methodCallTimeAdapter = new MethodCallTimeAdapter(cw);
        //get class reader
        ClassReader classReader = new ClassReader(className.replaceAll("/", "."));
        //accept
        classReader.accept(methodCallTimeAdapter, ClassReader.EXPAND_FRAMES);
        //return bytes
        return cw.toByteArray();
    }

}

