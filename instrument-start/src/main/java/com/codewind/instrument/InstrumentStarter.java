package com.codewind.instrument;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class InstrumentStarter {

    public static void main(String[] args) {
        SpringApplication.run(InstrumentStarter.class, args);
        TestTask testTask = new TestTask();
        testTask.sayHello();
    }

}
