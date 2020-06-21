package org.codewind.agent.asm;

import org.codewind.agent.asm.adpter.MethodCallTimeAdapter;
import org.junit.Test;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.util.TraceClassVisitor;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 方法调用耗时测试
 *
 * @author xuansu
 * @since 2020/6/20 9:13 下午
 */
public class MethodCallTimeAdapterTest {

    @Test
    public void testTimeCountAdpter() throws IOException {
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        TraceClassVisitor tv = new TraceClassVisitor(cw, new PrintWriter(System.out));
        MethodCallTimeAdapter addFiled = new MethodCallTimeAdapter(tv);
        ClassReader classReader = new ClassReader("org.codewind.agent.asm.util.TraceUtil");
        classReader.accept(addFiled, ClassReader.EXPAND_FRAMES);

        //输出至文件
//        File file = new File("TraceUtil.class");
//        file.createNewFile();
//        FileOutputStream fileOutputStream = new FileOutputStream(file);
//        fileOutputStream.write(cw.toByteArray());
    }
}
