package org.codewind.endpoint.task;

/**
 * 测试任务
 *
 * @author xuansu
 * @since 2020/6/20 11:06 下午
 */
public class TestInitTask {

    public void doTask() {
        System.out.println("task start ...");
        for (int i = 0; i < 100000; i++) {
        }
        System.out.println("task end.");
    }

}
