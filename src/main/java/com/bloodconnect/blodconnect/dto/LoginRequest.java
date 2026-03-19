package com.bloodconnect.blodconnect.dto;

import com.bloodconnect.blodconnect.enums.BloodType;
import lombok.Data;

@Data
public class LoginRequest {
    private String email;
    private String password;
    private String role;
}
