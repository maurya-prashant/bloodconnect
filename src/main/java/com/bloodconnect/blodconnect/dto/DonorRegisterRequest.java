package com.bloodconnect.blodconnect.dto;

import com.bloodconnect.blodconnect.enums.BloodType;
import lombok.Data;

@Data
public class DonorRegisterRequest {
    private String name;
    private String email;
    private String password;
    private BloodType bloodType;
    private String phone;
    private double latitude;
    private double longitude;
    private String city;
}
