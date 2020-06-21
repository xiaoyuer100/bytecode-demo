package org.codewind.endpoint;

import org.codewind.endpoint.task.TestInitTask;

/**
 * @author xuansu
 * @since 2020/6/20 11:06 下午
 */
public class ClientMain {

    public static void main(String[] args) {
        TestInitTask testInitTask = new TestInitTask();
        testInitTask.doTask();
    }
}
