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

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "vacancy_skill",
            joinColumns = {@JoinColumn(name = "vacancy_id")},
            inverseJoinColumns = {@JoinColumn(name = "skill_id")}
    )
    private Set<Skill> skills = new HashSet<>();

    @ManyToMany(mappedBy = "vacancies")
    private Set<User> users = new HashSet<>();

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
                "id=" + id +
                ", city=" + city +
                ", company='" + company + '\'' +
                ", wage='" + wage + '\'' +
                ", profession=" + profession +
                ", title='" + title + '\'' +
                ", url='" + url + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vacancy vacancy = (Vacancy) o;
        return Objects.equals(id, vacancy.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
