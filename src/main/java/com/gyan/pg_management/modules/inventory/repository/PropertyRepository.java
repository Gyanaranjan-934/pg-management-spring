package com.gyan.pg_management.modules.inventory.repository;

import com.gyan.pg_management.modules.inventory.domain.Property;
import com.gyan.pg_management.modules.identity.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PropertyRepository extends JpaRepository<Property, Long> {
    List<Property> findAllByOwner(User owner);
}
