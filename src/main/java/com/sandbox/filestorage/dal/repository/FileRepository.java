package com.sandbox.filestorage.dal.repository;

import com.sandbox.filestorage.dal.entity.FileEntity;

import java.util.List;

public interface FileRepository extends EnhancedJpaRepository<FileEntity, Long> {

    List<FileEntity> findByName(String name);

}
