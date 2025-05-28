package com.archive.articleArchive.app;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FileRepository extends JpaRepository<File, Long> {
    // Поиск по всем полям
    List<File> findByProjectNameContainingIgnoreCase(String projectName);
    List<File> findByUsernameContainingIgnoreCase(String username);
    List<File> findByUrlContainingIgnoreCase(String url);
    List<File> findByFacultyContainingIgnoreCase(String faculty);
    List<File> findByStudentGroupContainingIgnoreCase(String group);
    List<File> findByDepartmentContainingIgnoreCase(String department);
    List<File> findByProjectMasterNameContainingIgnoreCase(String projectMasterName);
}