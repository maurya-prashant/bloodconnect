package com.bloodconnect.blodconnect.service;

import com.bloodconnect.blodconnect.dto.AuthResponse;
import com.bloodconnect.blodconnect.dto.DonorRegisterRequest;
import com.bloodconnect.blodconnect.dto.HospitalRegisterRequest;
import com.bloodconnect.blodconnect.dto.LoginRequest;
import com.bloodconnect.blodconnect.entity.Donor;
import com.bloodconnect.blodconnect.entity.Hospital;
import com.bloodconnect.blodconnect.repository.DonorRepository;
import com.bloodconnect.blodconnect.repository.HospitalRepository;
import com.bloodconnect.blodconnect.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final DonorRepository donorRepository;
    private final HospitalRepository hospitalRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    //donor register
    public AuthResponse registerDonor(DonorRegisterRequest request){
        if (donorRepository.existsByEmail(request.getEmail())){
            throw new RuntimeException("Email already exists as donor");
        }

        Donor donor = new Donor();
        donor.setName(request.getName());
        donor.setEmail(request.getEmail());
        donor.setPassword(passwordEncoder.encode(request.getPassword()));
        donor.setBloodType(request.getBloodType());
        donor.setPhone(request.getPhone());
        donor.setLatitude(request.getLatitude());
        donor.setLongitude(request.getLongitude());
        donor.setCity(request.getCity());

        Donor saved = donorRepository.save(donor);

        String token = jwtUtil.generateToken(saved.getEmail(), "DONOR");
        return new AuthResponse(token, "DONOR", saved.getName(), saved.getId());
    }

    //hospital register

    public AuthResponse registerHospital(HospitalRegisterRequest request){
        if(hospitalRepository.existsByEmail(request.getEmail())){
            throw new RuntimeException("Email already registered as hospital");
        }
        Hospital hospital = new Hospital();
        hospital.setName(request.getName());
        hospital.setEmail(request.getEmail());
        hospital.setPassword(passwordEncoder.encode(request.getPassword()));
        hospital.setPhone(request.getPhone());
        hospital.setAddress(request.getAddress());
        hospital.setCity(request.getCity());
        hospital.setLatitude(request.getLatitude());
        hospital.setLongitude(request.getLongitude());

        Hospital saved = hospitalRepository.save(hospital);

        String token = jwtUtil.generateToken(saved.getEmail(), "HOSPITAL");
        return new AuthResponse(token, "HOSPITAL", saved.getName(), saved.getId());
    }

    //login

    public AuthResponse login(LoginRequest request) {
        if (request.getRole().equalsIgnoreCase("DONOR")) {
            Donor donor = donorRepository.findByEmail(request.getEmail())
                    .orElseThrow(() -> new RuntimeException("Donor not found"));

            if (!passwordEncoder.matches(request.getPassword(), donor.getPassword())) {
                throw new RuntimeException("Invalid password");
            }

            String token = jwtUtil.generateToken(donor.getEmail(), "DONOR");
            return new AuthResponse(token, "DONOR", donor.getName(), donor.getId());

        } else if (request.getRole().equalsIgnoreCase("HOSPITAL")) {
            Hospital hospital = hospitalRepository.findByEmail(request.getEmail())
                    .orElseThrow(() -> new RuntimeException("Hospital not found"));

            if (!passwordEncoder.matches(request.getPassword(), hospital.getPassword())) {
                throw new RuntimeException("Invalid password");
            }

            String token = jwtUtil.generateToken(hospital.getEmail(), "HOSPITAL");
            return new AuthResponse(token, "HOSPITAL", hospital.getName(), hospital.getId());

        } else {
            throw new RuntimeException("Invalid role");
        }
      }
}
