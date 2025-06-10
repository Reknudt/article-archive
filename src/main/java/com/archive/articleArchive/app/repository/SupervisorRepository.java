package com.archive.articleArchive.app.repository;

import com.archive.articleArchive.app.models.Student;
import com.archive.articleArchive.app.models.Supervisor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SupervisorRepository extends JpaRepository<Supervisor, Long> {
//    List<Student> findByFullNameContainingIgnoreCase(String name);
//    List<Student> findByFaculty(String faculty);
//    List<Student> findByGroupName(String groupName);
}