package com.gyan.pg_management.repository;

import com.gyan.pg_management.entity.Property;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PropertyRepository extends JpaRepository<Property, Long> {
}
