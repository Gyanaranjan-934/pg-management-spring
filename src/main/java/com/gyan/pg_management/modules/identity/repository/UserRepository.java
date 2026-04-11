package com.gyan.pg_management.modules.identity.repository;

import com.gyan.pg_management.modules.identity.domain.User;
import com.gyan.pg_management.modules.identity.domain.AuthProviderType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByProviderTypeAndProviderId(AuthProviderType providerType, String providerId);
    Optional<User> findByUsername(String username);
    @Query(value = "select u from User u where u.email = ?1 or u.username = ?1")
    Optional<User> findByUsernameOrEmail(String userData);
}
