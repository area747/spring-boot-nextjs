package com.app.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.CreationTimestamp;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class FileEntity {

    @Id
    @Column(columnDefinition = "BINARY(36)")
    private String id;

    @Column(nullable = false, columnDefinition = "VARCHAR(255) CHARACTER SET UTF8")
    private String fileName;

    @Column(nullable = true, columnDefinition = "VARCHAR(255) CHARACTER SET UTF8")
    private String extension;

    @CreationTimestamp
    @Column(nullable = false, columnDefinition = "DATETIME")
    private LocalDateTime inDate;

    // Getters, Setters, Constructors, and other methods
    // (생략)
}
