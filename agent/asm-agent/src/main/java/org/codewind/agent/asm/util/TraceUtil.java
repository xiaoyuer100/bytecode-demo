package org.codewind.agent.asm.util;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * Trace工具类
 *
 * @author xuansu
 * @since 2020/6/21 12:11 上午
 */
public class TraceUtil {

    /**
     * 方法调用耗时格式化输出
     *
     * @param className 全类名, example: java/lang/Runable
     * @param method    方法名称
     * @param time      调用耗时
     * @return
     */
    public static String formatMethodCallTime(String className, String method, long time) {
        BigDecimal bigDecimal = new BigDecimal(time / 1000000000.0).setScale(3, BigDecimal.ROUND_HALF_UP);
        String classFormat = className.replaceAll("/", ".");
        return String.format("%s.%s cost time: %s seconds", classFormat, method, bigDecimal);
    }

}
