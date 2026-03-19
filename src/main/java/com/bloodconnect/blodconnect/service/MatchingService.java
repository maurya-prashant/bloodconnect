package com.bloodconnect.blodconnect.service;

import com.bloodconnect.blodconnect.entity.BloodRequest;
import com.bloodconnect.blodconnect.entity.Donor;
import com.bloodconnect.blodconnect.entity.DonorResponse;
import com.bloodconnect.blodconnect.enums.BloodType;
import com.bloodconnect.blodconnect.enums.DonorResponseStatus;
import com.bloodconnect.blodconnect.repository.DonorRepository;
import com.bloodconnect.blodconnect.repository.DonorResponseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MatchingService {



    private final DonorRepository donorRepository;
    private final DonorResponseRepository donorResponseRepository;
    private final EmailService emailService;


    public List<DonorResponse> findAndNotifyDonors(BloodRequest request, double radiusKm) {

        // Step 1: Get all compatible blood types
        List<BloodType> compatibleTypes = getCompatibleBloodTypes(request.getBloodType());
        log.info("Looking for donors with blood types: {}", compatibleTypes);

        // Step 2: Fetch eligible donors from DB (available + right blood type + not donated recently)
        List<Donor> eligibleDonors = donorRepository.findEligibleDonors(compatibleTypes);
        log.info("Found {} eligible donors before distance filter", eligibleDonors.size());

        // Step 3: Filter by distance (within 5km of the hospital)
        double hospitalLat = request.getHospital().getLatitude();
        double hospitalLng = request.getHospital().getLongitude();

        List<Donor> nearbyDonors = eligibleDonors.stream()
                .filter(donor -> {
                    double distance = haversineDistance(
                            hospitalLat, hospitalLng,
                            donor.getLatitude(), donor.getLongitude()
                    );
                    return distance <= radiusKm;
                })
                .toList();

        log.info("Found {} donors within {}km", nearbyDonors.size(), radiusKm);

        // Step 4: Save DonorResponse records + send emails
        List<DonorResponse> responses = nearbyDonors.stream()
                .map(donor -> {
                    double distance = haversineDistance(
                            hospitalLat, hospitalLng,
                            donor.getLatitude(), donor.getLongitude()
                    );

                    // Save notification record
                    DonorResponse response = new DonorResponse();
                    response.setBloodRequest(request);
                    response.setDonor(donor);
                    response.setStatus(DonorResponseStatus.NOTIFIED);
                    response.setDistanceKm(distance);
                    response.setNotifiedAt(LocalDateTime.now());
                    DonorResponse saved = donorResponseRepository.save(response);

                    // Send email asynchronously
                    emailService.sendDonorNotification(donor, request, distance);

                    return saved;
                })
                .toList();

        log.info("Notified {} donors for blood request {}", responses.size(), request.getId());
        return responses;
    }

    /**
     * Haversine formula — calculates distance between two lat/lng points in km
     */
    private double haversineDistance(double lat1, double lng1, double lat2, double lng2) {
        final int EARTH_RADIUS_KM = 6371;

        double dLat = Math.toRadians(lat2 - lat1);
        double dLng = Math.toRadians(lng2 - lng1);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLng / 2) * Math.sin(dLng / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return EARTH_RADIUS_KM * c;
    }

    /**
     * Blood type compatibility — who can donate to whom
     */
    private List<BloodType> getCompatibleBloodTypes(BloodType needed) {
        return switch (needed) {
            case A_POSITIVE  -> List.of(BloodType.A_POSITIVE, BloodType.A_NEGATIVE,
                    BloodType.O_POSITIVE, BloodType.O_NEGATIVE);
            case A_NEGATIVE  -> List.of(BloodType.A_NEGATIVE, BloodType.O_NEGATIVE);
            case B_POSITIVE  -> List.of(BloodType.B_POSITIVE, BloodType.B_NEGATIVE,
                    BloodType.O_POSITIVE, BloodType.O_NEGATIVE);
            case B_NEGATIVE  -> List.of(BloodType.B_NEGATIVE, BloodType.O_NEGATIVE);
            case AB_POSITIVE -> List.of(BloodType.A_POSITIVE, BloodType.A_NEGATIVE,
                    BloodType.B_POSITIVE, BloodType.B_NEGATIVE,
                    BloodType.AB_POSITIVE, BloodType.AB_NEGATIVE,
                    BloodType.O_POSITIVE, BloodType.O_NEGATIVE);
            case AB_NEGATIVE -> List.of(BloodType.A_NEGATIVE, BloodType.B_NEGATIVE,
                    BloodType.AB_NEGATIVE, BloodType.O_NEGATIVE);
            case O_POSITIVE  -> List.of(BloodType.O_POSITIVE, BloodType.O_NEGATIVE);
            case O_NEGATIVE  -> List.of(BloodType.O_NEGATIVE);
        };
    }
}