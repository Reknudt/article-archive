package com.archive.articleArchive.app.models;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Entity
@NoArgsConstructor
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "ФИО обязательно")
    private String fullName;

    @Min(1) @Max(6)
    private Integer course;

    private String groupName;
    private String faculty;

    @ManyToOne
    @JoinColumn(name = "supervisor_id")
    private Supervisor supervisor;

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ScientificEvent> scientificEvents = new ArrayList<>();

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Publication> publications = new ArrayList<>();

    public void setId(Long id) {
        this.id = id;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setCourse(Integer course) {
        this.course = course;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public void setFaculty(String faculty) {
        this.faculty = faculty;
    }

    public void setSupervisor(Supervisor supervisor) {
        this.supervisor = supervisor;
    }

    public void setScientificEvents(List<ScientificEvent> scientificEvents) {
        this.scientificEvents = scientificEvents;
    }

    public void setPublications(List<Publication> publications) {
        this.publications = publications;
    }

    public Long getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public Integer getCourse() {
        return course;
    }

    public String getGroupName() {
        return groupName;
    }

    public String getFaculty() {
        return faculty;
    }

    public Supervisor getSupervisor() {
        return supervisor;
    }

    public List<ScientificEvent> getScientificEvents() {
        return scientificEvents;
    }

    public List<Publication> getPublications() {
        return publications;
    }
}