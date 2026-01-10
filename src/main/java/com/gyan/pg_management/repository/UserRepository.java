package com.gyan.pg_management.repository;

import com.gyan.pg_management.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {
}
