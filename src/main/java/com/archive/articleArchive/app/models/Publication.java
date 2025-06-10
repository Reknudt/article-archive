package com.archive.articleArchive.app.models;

import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "publication_type")
public abstract class Publication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String title;
    private String outputData;
    
    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;
}
//В полях ввода должны быть ФИО, курс, группа, факультет курсанта/студента,
//ФИО научного руководителя,
//кафедра, а также выбор научного мероприятия, которые делятся на виды, описанные ниже.
//
//научные мероприятия:
//1. конференции (в свою очередь делятся на круглые столы и семинары). Далее уровень мероприятия: они могут быть международные, республиканские, вузовские, после этого указывается название, дата проведения и результат.
//2. конкурсы - они делятся на международные, РКНР, ВАКР, вузовские, после этого указывается название, дата проведения и результат (для международных - лауреат, 1, 2, 3 место, без категории; для РКНР, ВАКР и вузовских просто поле для указания результата)
//
//В самой базе нужно сделать так, чтобы от ФИО курсанта/студента можно перейти к его публикациям, которые делятся на статьи (в свою очередь делятся на ВАК и иные) и тезисы
//
//Также должно быть пустое поле для ввода под названием "Выходные данные". Оно должно быть у каждой статьи, которую туда добавляют.
//
//Перейдя на какого-нибудь курсанта/студента, можно увидеть все его статьи и перейти по ним, где будет видно то, что выше описано