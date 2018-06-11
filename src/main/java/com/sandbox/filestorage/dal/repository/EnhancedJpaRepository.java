/**
 * Copyright (C) Zoomdata, Inc. 2012-2018. All rights reserved.
 */
package com.sandbox.filestorage.dal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import javax.persistence.EntityManager;
import java.io.Serializable;

@NoRepositoryBean
public interface EnhancedJpaRepository<T, ID extends Serializable>
    extends JpaRepository<T, ID> {

    T create(T model);

    void update(T model);

    T load(ID id);

    T saveOrUpdate(T model);

    EntityManager getEntityManager();
}
