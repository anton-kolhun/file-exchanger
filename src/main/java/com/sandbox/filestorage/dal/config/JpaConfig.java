package com.sandbox.filestorage.dal.config;

import com.sandbox.filestorage.dal.repository.EnhancedJpaRepositoryImpl;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EntityScan("com.sandbox.filestorage.dal.entity")
@EnableJpaRepositories(value = "com.sandbox.filestorage.dal.repository",
repositoryFactoryBeanClass = EnhancedJpaRepositoryImpl.EnhancedJpaRepositoryFactoryBean.class)
public class JpaConfig {

}
