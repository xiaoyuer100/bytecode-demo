package org.codewind.agent.asm;

import org.codewind.agent.asm.transformer.MethodCallTimeTransformer;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

/**
 * 方法调用耗时转换测试
 *
 * @author xuansu
 * @since 2020/6/20 10:39 下午
 */
public class MethodCallTimeTransformerTest {

    @Test
    public void testMethodTimeCountTransformer() throws IOException {
        MethodCallTimeTransformer methodCallTimeTransformer = new MethodCallTimeTransformer(null);
        byte[] bytes = methodCallTimeTransformer.transformClass("org.codewind.agent.asm.PreMainTraceAgent".replaceAll("\\.", "/"));
        Assert.assertNotNull(bytes);
        Assert.assertTrue(bytes.length > 0);
    }
}
