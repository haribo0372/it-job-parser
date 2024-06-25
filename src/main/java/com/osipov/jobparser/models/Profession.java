package com.osipov.jobparser.models;

import jakarta.persistence.*;
import lombok.Data;

@Entity(name = "profession")
@Data
public class Profession {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public Long id;

    @Column(name = "name")
    public String name;
}
