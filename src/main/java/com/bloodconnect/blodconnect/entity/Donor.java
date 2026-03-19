package com.bloodconnect.blodconnect.entity;

import com.bloodconnect.blodconnect.enums.BloodType;
import com.bloodconnect.blodconnect.enums.Role;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "donors")
@Data
public class Donor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BloodType bloodType;

    @Column(nullable = false)
    private String phone;

    @Column(nullable = false)
    private Double latitude;

    @Column(nullable = false)
    private Double longitude;

    @Column(nullable = false)
    private String city;

    private LocalDate lastDonationDate;

    @Column(nullable = false)
    private Boolean isAvailable = true;

    @Enumerated(EnumType.STRING)
    private Role role = Role.DONOR;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

}
