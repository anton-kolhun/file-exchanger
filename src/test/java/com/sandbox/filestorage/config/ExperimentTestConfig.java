package com.sandbox.filestorage.config;

import org.springframework.context.annotation.Bean;

//@Configuration
//Import(ExperimentTestConfig.InnerClass1.class)
public class ExperimentTestConfig {

    @Bean
    public Object test() {
        return new Object();
    }


    public static class InnerClass2 {

        @Bean
        public Object test2() {
            return new Object();
        }

    }

    public static class InnerClass1 {

        @Bean
        public Object test2() {
            return new Object();
        }

    }




}
