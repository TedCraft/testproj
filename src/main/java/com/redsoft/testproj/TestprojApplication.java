package com.redsoft.testproj;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class TestprojApplication {

    public static void main(String[] args) {
        SpringApplication.run(TestprojApplication.class, args);
        Init init = new Init();
        init.createDb();
    }
}
