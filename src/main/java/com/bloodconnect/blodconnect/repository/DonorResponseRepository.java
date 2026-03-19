package com.bloodconnect.blodconnect.repository;
import com.bloodconnect.blodconnect.entity.Donor;
import com.bloodconnect.blodconnect.entity.DonorResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DonorResponseRepository extends JpaRepository<DonorResponse, Long> {
    List<DonorResponse> findByBloodRequestId(Long bloodRequestId);
    List<DonorResponse> findByDonorId(Long donorId);
    Optional<DonorResponse> findByBloodRequestIdAndDonorId(Long bloodRequestId, Long DonorId);
}
