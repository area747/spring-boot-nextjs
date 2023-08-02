package com.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.entity.FileEntity;

public interface FileRepository extends JpaRepository<FileEntity, Long> {
    // 기본적인 CRUD 메서드는 JpaRepository에서 제공됩니다.
}