package com.archive.articleArchive.app;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class File {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String projectName;
    private String username;
    private String url;
    private String faculty;
    private String studentGroup;
    private String department;
    private String projectMasterName;

    public Long getId() {
        return id;
    }

    public String getProjectName() {
        return projectName;
    }

    public String getUsername() {
        return username;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFaculty() {
        return faculty;
    }

    public String getStudentGroup() {
        return studentGroup;
    }

    public String getDepartment() {
        return department;
    }

    public String getProjectMasterName() {
        return projectMasterName;
    }

    public void setFaculty(String faculty) {
        this.faculty = faculty;
    }

    public void setStudentGroup(String studentGroup) {
        this.studentGroup = studentGroup;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public void setProjectMasterName(String projectMasterName) {
        this.projectMasterName = projectMasterName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}