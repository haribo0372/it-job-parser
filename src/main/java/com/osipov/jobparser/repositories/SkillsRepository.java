package com.osipov.jobparser.repositories;

import com.osipov.jobparser.models.Skill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SkillsRepository extends JpaRepository<Skill, Long> {
    List<Skill> findByName(String name);
}
