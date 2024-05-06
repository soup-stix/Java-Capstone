package com.anand.capstone.repository;

import com.anand.capstone.entity.Credentials;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CredentialsRepository extends JpaRepository<Credentials, Long> {

    Optional<Credentials> findByEmail(String email);
    void deleteByEmail(String email);
}
