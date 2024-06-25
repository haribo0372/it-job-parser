package com.osipov.jobparser.repositories;

import com.osipov.jobparser.models.Skills;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SkillsRepository extends JpaRepository<Skills, Long> {
}
