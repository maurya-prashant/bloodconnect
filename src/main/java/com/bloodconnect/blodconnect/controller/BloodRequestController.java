package com.bloodconnect.blodconnect.controller;

import com.bloodconnect.blodconnect.dto.BloodRequestDTO;
import com.bloodconnect.blodconnect.entity.BloodRequest;
import com.bloodconnect.blodconnect.entity.Donor;
import com.bloodconnect.blodconnect.entity.DonorResponse;
import com.bloodconnect.blodconnect.service.BloodRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.bloodconnect.blodconnect.entity.Donor;

import java.util.List;

@RestController
@RequestMapping("/api/v1/hospital")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class BloodRequestController {

    private final BloodRequestService bloodRequestService;

    @PostMapping("/{hospitalId}/request")
    public ResponseEntity<BloodRequest> createRequest(
            @PathVariable Long hospitalId,
            @RequestBody BloodRequestDTO dto) {
        return ResponseEntity.ok(bloodRequestService.createRequest(hospitalId, dto));
    }

    @GetMapping("/{hospitalId}/requests")
    public ResponseEntity<List<BloodRequest>> getHospitalRequests(
            @PathVariable Long hospitalId) {
        return ResponseEntity.ok(bloodRequestService.getHospitalRequests(hospitalId));
    }

    @GetMapping("/request/{requestId}")
    public ResponseEntity<BloodRequest> getRequestById(
            @PathVariable Long requestId) {
        return ResponseEntity.ok(bloodRequestService.getRequestById(requestId));
    }

    @PutMapping("/{hospitalId}/request/{requestId}/cancel")
    public ResponseEntity<BloodRequest> cancelRequest(
            @PathVariable Long hospitalId,
            @PathVariable Long requestId) {
        return ResponseEntity.ok(bloodRequestService.cancelRequest(requestId, hospitalId));
    }

    @GetMapping("/{hospitalId}/responses")
    public ResponseEntity<List<DonorResponse>> getHospitalResponses(
            @PathVariable Long hospitalId) {
        return ResponseEntity.ok(bloodRequestService.getHospitalResponses(hospitalId));
    }

    @GetMapping("/{hospitalId}/donors")
    public ResponseEntity<List<Donor>> getNearbyDonors(@PathVariable Long hospitalId) {
        return ResponseEntity.ok(bloodRequestService.getNearbyDonors(hospitalId));
    }
}