package com.sandbox.filestorage;

import com.sandbox.filestorage.config.ExperimentTestConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = ExperimentTestConfig.class)
public class ExperimentTest {


    @Test
    public void test() {
        System.out.println("test");
    }
}
