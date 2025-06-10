package com.archive.articleArchive.app.repository;

import com.archive.articleArchive.app.models.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Long> {
    List<Student> findByFullNameContainingIgnoreCase(String name);
    List<Student> findByFaculty(String faculty);
    List<Student> findByGroupName(String groupName);
    List<Student> findDistinctFaculties();
    List<Student> findDistinctGroupNames();
}