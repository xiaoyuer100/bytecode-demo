package com.codewind.instrument.task;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

/**
 * @author xuansu
 * @since 2020/6/20 9:53 下午
 */
@Component
public class TestInitTask implements InitializingBean {

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("init print task");
    }

}
