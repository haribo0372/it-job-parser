package com.osipov.jobparser.models;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Objects;
import java.util.Set;

@Entity(name = "skill")
@Data
public class Skill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public Long id;

    @Column(name = "name")
    public String name;

    @ManyToMany(mappedBy = "vacancies")
    private Set<Vacancy> vacancies;

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Skill skill = (Skill) object;
        return Objects.equals(name, skill.name);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }
}
