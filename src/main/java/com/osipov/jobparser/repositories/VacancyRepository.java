package com.osipov.jobparser.repositories;

import com.osipov.jobparser.models.Vacancy;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VacancyRepository extends JpaRepository<Vacancy, Long> {
}
