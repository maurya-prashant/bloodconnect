package com.bloodconnect.blodconnect.dto;

import com.bloodconnect.blodconnect.enums.BloodType;
import com.bloodconnect.blodconnect.enums.UrgencyLevel;
import lombok.Data;

@Data
public class BloodRequestDTO {
    private BloodType bloodType;
    private UrgencyLevel urgencyLevel;
    private Integer unitsNeeded;
    private String notes;
    private Double radiusKm; // optional, defaults to 5.0
}
