package com.bloodconnect.blodconnect.entity;

import com.bloodconnect.blodconnect.enums.BloodType;
import com.bloodconnect.blodconnect.enums.RequestStatus;
import com.bloodconnect.blodconnect.enums.UrgencyLevel;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "blood_requests")
@Data
public class BloodRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @jakarta.persistence.JoinColumn(name = "hospital_id")
    private Hospital hospital;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BloodType bloodType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UrgencyLevel urgencyLevel;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RequestStatus status = RequestStatus.PENDING;

    @Column(nullable = false)
    private Integer unitsNeeded;

    private String notes;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime fulfilledAt;
}
