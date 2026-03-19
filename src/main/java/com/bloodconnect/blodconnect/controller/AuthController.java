package com.bloodconnect.blodconnect.controller;

import com.bloodconnect.blodconnect.dto.AuthResponse;
import com.bloodconnect.blodconnect.dto.DonorRegisterRequest;
import com.bloodconnect.blodconnect.dto.HospitalRegisterRequest;
import com.bloodconnect.blodconnect.dto.LoginRequest;
import com.bloodconnect.blodconnect.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register/donor")
    public ResponseEntity<AuthResponse> registerDonor(@RequestBody DonorRegisterRequest request){
        return ResponseEntity.ok(authService.registerDonor(request));
    }

    @PostMapping("/register/hospital")
    public ResponseEntity<AuthResponse> registerHospital(@RequestBody HospitalRegisterRequest request) {
        return ResponseEntity.ok(authService.registerHospital(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }
}
