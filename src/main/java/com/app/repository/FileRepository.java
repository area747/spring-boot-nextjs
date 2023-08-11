package com.app.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.entity.FileEntity;

public interface FileRepository extends JpaRepository<FileEntity, Long> {
    Optional<FileEntity> findById(String id);
    Optional<List<FileEntity>> findAllByOrderByInDateAsc();
}