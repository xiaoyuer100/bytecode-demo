package util;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * @author xuansu
 * @since 2020/6/21 12:11 上午
 */
public class TimeUtil {

    public static Map<String, Long> startTime = new HashMap<>();
    public static Map<String, Long> endTime = new HashMap<>();

    public static void setStartTime(String method, Long start) {
        startTime.put(method, start);
    }

    public static void setEndTime(String method, Long end) {
        endTime.put(method, end);
    }

    public static String costTimeMethod(String method) {
        long start = startTime.get(method);
        long end = endTime.get(method);
        double cost = (end - start) / 1000000.0;
        return "[" + (cost) + " ms]";
    }

    public static String traceMethodCostTime(String className, String method, long time) {
        BigDecimal bigDecimal = new BigDecimal(time / 1000000000.0).setScale(3, BigDecimal.ROUND_HALF_UP);
        String classFormat = className.replaceAll("/", ".");
        return String.format("%s.%s cost time: %s seconds", classFormat, method, bigDecimal);
    }

}
