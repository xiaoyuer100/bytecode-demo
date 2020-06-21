package org.codewind.agent.asm;

import org.codewind.agent.asm.transformer.MethodCallTimeTransformer;

import java.lang.instrument.Instrumentation;

/**
 * agent 入口
 *
 * @author xuansu
 * @since 2020/6/20 7:38 下午
 */
public class PreMainTraceAgent {

    public static void premain(String agentArgs, Instrumentation inst) {
        //print agent args
        System.out.println("asm agent args : " + agentArgs);
        //添加方法耗时转换器
        inst.addTransformer(new MethodCallTimeTransformer(agentArgs));
    }

}
