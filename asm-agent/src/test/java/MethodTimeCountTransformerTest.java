import org.codewind.agent.asm.transformer.MethodTimeCountTransformer;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

/**
 * @author xuansu
 * @since 2020/6/20 10:39 下午
 */
public class MethodTimeCountTransformerTest {

    @Test
    public void testMethodTimeCountTransformer() throws IOException {
        MethodTimeCountTransformer methodTimeCountTransformer = new MethodTimeCountTransformer(null);
        byte[] bytes = methodTimeCountTransformer.transformClass("org.codewind.agent.asm.PreMainTraceAgent".replaceAll("\\.", "/"));
        Assert.assertTrue(bytes.length > 0);
    }
}
