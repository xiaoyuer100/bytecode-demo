import org.codewind.agent.asm.adpter.TimeCountAdpter;
import org.junit.Test;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.util.TraceClassVisitor;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author xuansu
 * @since 2020/6/20 9:13 下午
 */
public class TimeCountAdpterTest {

    @Test
    public void testTimeCountAdpter() throws IOException {
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
//        TraceClassVisitor tv=new TraceClassVisitor(cw,new PrintWriter(System.out));
        TimeCountAdpter addFiled = new TimeCountAdpter(cw);
        ClassReader classReader = new ClassReader("Cat");
        classReader.accept(addFiled, ClassReader.EXPAND_FRAMES);

        File file = new File("Cat.class");
        file.createNewFile();
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        fileOutputStream.write(cw.toByteArray());
    }
}
