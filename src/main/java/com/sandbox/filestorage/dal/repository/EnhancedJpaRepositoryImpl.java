/**
 * Copyright (C) Zoomdata, Inc. 2012-2018. All rights reserved.
 */
package com.sandbox.filestorage.dal.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactoryBean;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.io.Serializable;

//@Repository
@Transactional(readOnly = true)
public class EnhancedJpaRepositoryImpl<T, ID extends Serializable> extends SimpleJpaRepository<T, ID> implements EnhancedJpaRepository<T, ID> {

    @Autowired
    private EntityManager entityManager;


    public EnhancedJpaRepositoryImpl(JpaEntityInformation<T, ?> entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager);
        this.entityManager = entityManager;
    }

    public EnhancedJpaRepositoryImpl(Class<T> domainClass, EntityManager em) {
        super(domainClass, em);
        this.entityManager = em;
    }

    @Override
    public EntityManager getEntityManager() {
        return entityManager;
    }

    @Transactional
    @Override
    public T create(T model) {
        return saveAndFlush(model);
    }

    @Transactional
    @Override
    public void update(T model) {
        saveAndFlush(model);
    }

    @Override
    public T load(ID id) {
        return findById(id).get();
    }



    @Transactional
    @Override
    public T saveOrUpdate(T model) {
        return saveAndFlush(model);
    }

    public static class EnhancedJpaRepositoryFactoryBean
            <T extends org.springframework.data.repository.Repository<S, ID>, S, ID extends Serializable>
            extends JpaRepositoryFactoryBean<T, S, ID> {


        public EnhancedJpaRepositoryFactoryBean(Class<? extends T> repositoryInterface) {
            super(repositoryInterface);
        }

        @Override
        protected RepositoryFactorySupport createRepositoryFactory(EntityManager entityManager) {
            return new JpaRepositoryFactory(entityManager) {
                @Override
                protected Class<?> getRepositoryBaseClass(RepositoryMetadata metadata) {
                    return EnhancedJpaRepositoryImpl.class;
                }
            };
        }
    }
}
