package com.osipov.jobparser.models;

import jakarta.persistence.*;
import lombok.Data;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity(name = "vacancy")
@Data
public class Vacancy {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "city_id")
    private City city;

    @Column(name = "company")
    private String company;

    @Column(name = "wage")
    private String wage;

    @ManyToOne
    @JoinColumn(name = "profession_id")
    private Profession profession;

    @Column(name = "title")
    private String title;

    @Column(name = "url")
    private String url;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "vacancy_skill",
            joinColumns = {@JoinColumn(name = "vacancy_id")},
            inverseJoinColumns = {@JoinColumn(name = "skill_id")}
    )
    private Set<Skill> skills = new HashSet<>();

    public void addSkill(Skill skill) {
        skills.add(skill);
    }

    public boolean skillExists(Skill skill) {
        for (Skill skill1 : skills) {
            if (skill1.equals(skill)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return "Vacancy{" +
                "url='" + url + '\'' +
                ", company='" + company + '\'' +
                ", profession=" + profession +
                ", title='" + title + '\'' +
                ", city='" + city + '\'' +
                ", wage='" + wage + '\'' +
                ", skills=" + skills +
                '}';
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Vacancy vacancy = (Vacancy) object;
        return Objects.equals(url, vacancy.url);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(url);
    }
}
