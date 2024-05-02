package com.anand.capstone.repository;

import com.anand.capstone.entity.Credentials;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CredentialsRepository extends JpaRepository<Credentials, Long> {

}
