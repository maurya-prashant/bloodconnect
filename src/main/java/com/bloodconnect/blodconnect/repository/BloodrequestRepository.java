package com.bloodconnect.blodconnect.repository;

import com.bloodconnect.blodconnect.entity.BloodRequest;
import com.bloodconnect.blodconnect.enums.RequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BloodrequestRepository extends JpaRepository<BloodRequest, Long> {
    List<BloodRequest> findByHospitalId(Long hospitalId);
    List<BloodRequest> findByStatus(RequestStatus status);

}