package com.archive.articleArchive.app.models;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@DiscriminatorValue("CONFERENCE")
public class Conference extends ScientificEvent {

    public void setConferenceType(ConferenceType conferenceType) {
        this.conferenceType = conferenceType;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public ConferenceType getConferenceType() {
        return conferenceType;
    }

    public Level getLevel() {
        return level;
    }

    public enum ConferenceType { ROUND_TABLE, SEMINAR }
    public enum Level { INTERNATIONAL, REPUBLICAN, UNIVERSITY }
    
    @Enumerated(EnumType.STRING)
    private ConferenceType conferenceType;
    
    @Enumerated(EnumType.STRING)
    private Level level;
}