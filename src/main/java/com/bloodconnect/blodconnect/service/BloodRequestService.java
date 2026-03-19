package com.bloodconnect.blodconnect.service;

import com.bloodconnect.blodconnect.dto.BloodRequestDTO;
import com.bloodconnect.blodconnect.entity.BloodRequest;
import com.bloodconnect.blodconnect.entity.Donor;
import com.bloodconnect.blodconnect.entity.DonorResponse;
import com.bloodconnect.blodconnect.entity.Hospital;
import com.bloodconnect.blodconnect.enums.RequestStatus;
import com.bloodconnect.blodconnect.repository.BloodrequestRepository;
import com.bloodconnect.blodconnect.repository.DonorRepository;
import com.bloodconnect.blodconnect.repository.DonorResponseRepository;
import com.bloodconnect.blodconnect.repository.HospitalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BloodRequestService {
    private final BloodrequestRepository bloodRequestRepository;
    private final HospitalRepository hospitalRepository;
    private final MatchingService matchingService;
    private final DonorResponseRepository donorResponseRepository;
    private final DonorRepository donorRepository;

//    public BloodRequest createRequest(Long hospitalId, BloodRequestDTO dto) {
//
//        Hospital hospital = hospitalRepository.findById(hospitalId)
//                .orElseThrow(() -> new RuntimeException("Hospital not found"));
//
//        // create the blood request
//        BloodRequest request = new BloodRequest();
//        request.setHospital(hospital);
//        request.setBloodType(dto.getBloodType());
//        request.setUrgencyLevel(dto.getUrgencyLevel());
//        request.setUnitsNeeded(dto.getUnitsNeeded());
//        request.setNotes(dto.getNotes());
//        request.setStatus(RequestStatus.PENDING);
//
//        BloodRequest saved = bloodRequestRepository.save(request);
//
//        // immediately trigger matching algorithm
//        List<DonorResponse> notifiedDonors = matchingService.findAndNotifyDonors(saved);
//
//        System.out.println("Notified " + notifiedDonors.size() + " donors for request " + saved.getId());
//
//        return saved;
//    }

    public BloodRequest createRequest(Long hospitalId, BloodRequestDTO dto) {
        Hospital hospital = hospitalRepository.findById(hospitalId)
                .orElseThrow(() -> new RuntimeException("Hospital not found"));

        BloodRequest request = new BloodRequest();
        request.setHospital(hospital);
        request.setBloodType(dto.getBloodType());
        request.setUrgencyLevel(dto.getUrgencyLevel());
        request.setUnitsNeeded(dto.getUnitsNeeded());
        request.setNotes(dto.getNotes());
        request.setStatus(RequestStatus.PENDING);

        BloodRequest saved = bloodRequestRepository.save(request);

        // use provided radius or default to 5.0
        double radius = (dto.getRadiusKm() != null) ? dto.getRadiusKm() : 5.0;
        List<DonorResponse> notifiedDonors = matchingService.findAndNotifyDonors(saved, radius);

        System.out.println("Notified " + notifiedDonors.size() + " donors within " + radius + "km for request " + saved.getId());

        return saved;
    }

    public List<BloodRequest> getHospitalRequests(Long hospitalId) {
        return bloodRequestRepository.findByHospitalId(hospitalId);
    }

    public BloodRequest getRequestById(Long requestId) {
        return bloodRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found"));
    }

    public BloodRequest updateStatus(Long requestId, RequestStatus status) {
        BloodRequest request = getRequestById(requestId);
        request.setStatus(status);
        return bloodRequestRepository.save(request);
    }

    //cancel request
    public BloodRequest cancelRequest(Long requestId, Long hospitalId) {
        BloodRequest request = getRequestById(requestId);

        if (!request.getHospital().getId().equals(hospitalId)) {
            throw new RuntimeException("Unauthorized to cancel this request");
        }
        if (request.getStatus() == RequestStatus.FULFILLED) {
            throw new RuntimeException("Cannot cancel a fulfilled request");
        }

        request.setStatus(RequestStatus.CANCELLED);
        return bloodRequestRepository.save(request);
    }

    public List<Donor> getNearbyDonors(Long hospitalId) {
        Hospital hospital = hospitalRepository.findById(hospitalId)
                .orElseThrow(() -> new RuntimeException("Hospital not found"));

        List<Donor> allDonors = donorRepository.findAll();

        return allDonors.stream()
                .filter(donor -> {
                    double distance = HaversineUtil.calculateDistance(
                            hospital.getLatitude(), hospital.getLongitude(),
                            donor.getLatitude(), donor.getLongitude()
                    );
                    return distance <= 5.0;
                })
                .toList();
    }

    public List<DonorResponse> getHospitalResponses(Long hospitalId) {
        List<BloodRequest> requests = bloodRequestRepository.findByHospitalId(hospitalId);
        return requests.stream()
                .flatMap(r -> donorResponseRepository.findByBloodRequestId(r.getId()).stream())
                .toList();
    }
}
