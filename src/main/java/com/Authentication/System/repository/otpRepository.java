package com.Authentication.System.repository;

import com.Authentication.System.entity.otp;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface otpRepository extends JpaRepository<otp, Integer> {
    Optional<otp> findByEmail(String email);
}
