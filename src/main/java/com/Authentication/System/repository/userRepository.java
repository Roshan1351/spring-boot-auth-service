package com.Authentication.System.repository;

import com.Authentication.System.entity.user;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface userRepository extends JpaRepository<user, Integer> {

    boolean existsByUsername(String email);

    boolean existsByEmail(String email);

    Optional<user> findByEmail(String email);

    Optional<user> findByUsername(String username);
}
