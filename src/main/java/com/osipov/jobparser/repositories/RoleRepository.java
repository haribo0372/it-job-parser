package com.osipov.jobparser.repositories;

import com.osipov.jobparser.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
}
