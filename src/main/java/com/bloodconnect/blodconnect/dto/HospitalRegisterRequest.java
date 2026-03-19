package com.bloodconnect.blodconnect.dto;

import com.bloodconnect.blodconnect.enums.BloodType;
import lombok.Data;

@Data
public class HospitalRegisterRequest {
    private String name;
    private String email;
    private String password;
    private String phone;
    private String address;
    private String city;
    private Double latitude;
    private Double longitude;

}
