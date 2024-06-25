package com.osipov.jobparser.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

@Entity(name = "vacancy")
@Data
public class Vacancy {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;

    @Column(name = "city")
    private String city;

    @Column(name = "company")
    private String company;

    @Column(name = "title")
    private String title;

    @Column(name = "skills")
    private String skills;

    @Column(name = "url")
    private String url;
}
