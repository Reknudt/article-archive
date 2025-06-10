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
@DiscriminatorValue("CONTEST")
public class Contest extends ScientificEvent {
    public enum ContestType { INTERNATIONAL, RKNR, VAKR, UNIVERSITY }
    
    @Enumerated(EnumType.STRING)
    private ContestType contestType;
}