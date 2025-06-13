package com.archive.articleArchive.app.repository;

import com.archive.articleArchive.app.models.Student;
import org.hibernate.annotations.processing.Find;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Long> {
    List<Student> findByFullNameContainingIgnoreCase(String name);
    List<Student> findByFaculty(String faculty);
    List<Student> findByGroupName(String groupName);
    @Query("SELECT DISTINCT s.faculty FROM Student s")
    List<String> findDistinctFaculties();

    @Query("SELECT DISTINCT s.groupName FROM Student s")
    List<String> findDistinctGroupNames();
//    @Find
//    List<Student> findAllStudents();
}