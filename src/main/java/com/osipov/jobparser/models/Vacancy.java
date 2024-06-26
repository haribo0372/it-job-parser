package com.osipov.jobparser.models;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

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
    @ManyToMany(cascade = { CascadeType.ALL })
    @JoinTable(
            name = "vacancy_skill",
            joinColumns = { @JoinColumn(name = "vacancy_id") },
            inverseJoinColumns = { @JoinColumn(name = "skill_id") }
    )
    private Set<Skill> skills;

    @Column(name = "url")
    private String url;

    public void addSkill(Skill skill){
        skills.add(skill);
    }

    public boolean skillExists(Skill skill){
        for (Skill skill1 : skills){
            if (skill1.equals(skill)){
                return true;
            }
        }
        return false;
    }
}
