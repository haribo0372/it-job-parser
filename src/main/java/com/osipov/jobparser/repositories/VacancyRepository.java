package com.osipov.jobparser.repositories;

import com.osipov.jobparser.models.Vacancy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VacancyRepository extends JpaRepository<Vacancy, Long> {
    Optional<Vacancy> findByUrl(String url);
}
