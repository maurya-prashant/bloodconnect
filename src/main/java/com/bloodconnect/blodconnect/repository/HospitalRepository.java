package com.bloodconnect.blodconnect.repository;

import com.bloodconnect.blodconnect.entity.Hospital;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HospitalRepository extends JpaRepository<Hospital, Long> {
    Optional<Hospital> findByEmail(String email);

    boolean existsByEmail(String email);
}
