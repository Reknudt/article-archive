package com.archive.articleArchive.app.models;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@DiscriminatorValue("THESIS")
public class Thesis extends Publication {

    private String conferenceName;
    private LocalDate conferenceDate;

    public void setConferenceName(String conferenceName) {
        this.conferenceName = conferenceName;
    }

    public void setConferenceDate(LocalDate conferenceDate) {
        this.conferenceDate = conferenceDate;
    }

    public String getConferenceName() {
        return conferenceName;
    }

    public LocalDate getConferenceDate() {
        return conferenceDate;
    }
}