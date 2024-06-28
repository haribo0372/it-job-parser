package com.osipov.jobparser.repositories;

import com.osipov.jobparser.models.City;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CityRepository extends JpaRepository<City, Long> {
    Optional<City> findByName(String cityName);
}
