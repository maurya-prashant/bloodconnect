package com.bloodconnect.blodconnect.controller;

import com.bloodconnect.blodconnect.dto.DonorResponseDTO;
import com.bloodconnect.blodconnect.entity.Donor;
import com.bloodconnect.blodconnect.entity.DonorResponse;
import com.bloodconnect.blodconnect.service.DonorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/donor")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class DonorController {

    private final DonorService donorService;

    @GetMapping("/{donorId}/notifications")
    public ResponseEntity<List<DonorResponse>> getDonorNotifications(
            @PathVariable Long donorId) {
        return ResponseEntity.ok(donorService.getDonorNotifications(donorId));
    }

    @PutMapping("/{donorId}/respond")
    public ResponseEntity<DonorResponse> respondToRequest(
            @PathVariable Long donorId,
            @RequestBody DonorResponseDTO dto) {
        return ResponseEntity.ok(donorService.respondToRequest(donorId, dto));
    }

    @PutMapping("/{donorId}/availability")
    public ResponseEntity<Donor> updateAvailability(
            @PathVariable Long donorId,
            @RequestParam Boolean isAvailable) {
        return ResponseEntity.ok(donorService.updateAvailability(donorId, isAvailable));
    }
}