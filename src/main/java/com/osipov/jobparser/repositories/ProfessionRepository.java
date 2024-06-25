package com.osipov.jobparser.repositories;

import com.osipov.jobparser.models.Profession;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfessionRepository extends JpaRepository<Profession, Long> {
}
