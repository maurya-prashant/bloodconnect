package com.bloodconnect.blodconnect.service;

import com.bloodconnect.blodconnect.dto.DonorResponseDTO;
import com.bloodconnect.blodconnect.entity.Donor;
import com.bloodconnect.blodconnect.entity.DonorResponse;
import com.bloodconnect.blodconnect.enums.DonorResponseStatus;
import com.bloodconnect.blodconnect.repository.DonorRepository;
import com.bloodconnect.blodconnect.repository.DonorResponseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DonorService {

    private final DonorRepository donorRepository;
    private final DonorResponseRepository donorResponseRepository;

    public List<DonorResponse> getDonorNotifications(Long donorId) {
        return donorResponseRepository.findByDonorId(donorId);
    }

    public DonorResponse respondToRequest(Long donorId, DonorResponseDTO dto) {
        DonorResponse response = donorResponseRepository
                .findByBloodRequestIdAndDonorId(dto.getBloodRequestId(), donorId)
                .orElseThrow(() -> new RuntimeException("Notification not found"));

        response.setStatus(dto.getDonorResponseStatus());
        response.setRespondedAt(LocalDateTime.now());

        // if donor accepted update their availability
        if (dto.getDonorResponseStatus() == DonorResponseStatus.ACCEPTED) {
            Donor donor = response.getDonor();
            donor.setIsAvailable(false);
            donorRepository.save(donor);
        }

        return donorResponseRepository.save(response);
    }

    public Donor updateAvailability(Long donorId, Boolean isAvailable) {
        Donor donor = donorRepository.findById(donorId)
                .orElseThrow(() -> new RuntimeException("Donor not found"));
        donor.setIsAvailable(isAvailable);
        return donorRepository.save(donor);
    }
}