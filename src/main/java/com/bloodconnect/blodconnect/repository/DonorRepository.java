package com.bloodconnect.blodconnect.repository;

import com.bloodconnect.blodconnect.entity.Donor;
import com.bloodconnect.blodconnect.enums.BloodType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DonorRepository extends JpaRepository<Donor, Long> {
    Optional<Donor> findByEmail(String email);
    boolean existsByEmail(String email);

    @Query("SELECT d FROM Donor d WHERE d.bloodType IN :bloodTypes AND d.isAvailable = true AND (d.lastDonationDate IS NULL OR d.lastDonationDate <= LOCAL DATE - 56 DAY)")
    List<Donor> findEligibleDonors(@Param("bloodTypes") List<BloodType> bloodTypes);

    List<Donor> findAll();

//    @Query(value = "SELECT * FROM donors WHERE blood_type IN :bloodTypes AND is_available = true AND (last_donation_date IS NULL OR last_donation_date <= CURRENT_DATE - INTERVAL '56 days')", nativeQuery = true)
//    List<Donor> findEligibleDonors(@Param("bloodTypes") List<String> bloodTypes);

}
