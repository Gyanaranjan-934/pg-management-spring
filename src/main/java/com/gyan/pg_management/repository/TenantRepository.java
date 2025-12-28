package com.gyan.pg_management.repository;

import com.gyan.pg_management.entity.Tenant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TenantRepository extends JpaRepository<Tenant, Long> {
}
