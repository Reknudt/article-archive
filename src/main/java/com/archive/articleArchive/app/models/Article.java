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
@DiscriminatorValue("ARTICLE")
public class Article extends Publication {
    public enum ArticleType { VAK, OTHER }
    
    @Enumerated(EnumType.STRING)
    private ArticleType articleType;
}