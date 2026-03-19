package com.bloodconnect.blodconnect.entity;

import com.bloodconnect.blodconnect.enums.DonorResponseStatus;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Generated;

import java.time.LocalDateTime;

@Entity
@Table(name = "donor_responses")
@Data
public class DonorResponse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "blood_request_id", nullable = false)
    private BloodRequest bloodRequest;

    @ManyToOne
    @JoinColumn(name = "donor_id", nullable = false)
    private Donor donor;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DonorResponseStatus status = DonorResponseStatus.NOTIFIED;

    @Column(nullable = false)
    private Double distanceKm;

    @Column(nullable = false)
    private LocalDateTime notifiedAt = LocalDateTime.now();

    private LocalDateTime respondedAt;
}
