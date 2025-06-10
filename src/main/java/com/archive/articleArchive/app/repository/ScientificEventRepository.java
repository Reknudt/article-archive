package com.archive.articleArchive.app.repository;

import com.archive.articleArchive.app.models.ScientificEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScientificEventRepository extends JpaRepository<ScientificEvent, Long> {
    List<ScientificEvent> findByStudentId(Long studentId);
}