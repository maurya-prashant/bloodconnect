package com.bloodconnect.blodconnect.service;

import com.bloodconnect.blodconnect.entity.BloodRequest;
import com.bloodconnect.blodconnect.entity.Donor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;

    @Async
    public void sendDonorNotification(Donor donor, BloodRequest request, double distanceKm) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(donor.getEmail());
            message.setSubject("🩸 Urgent Blood Donation Request - " + request.getBloodType() + " Needed");
            message.setText(buildEmailBody(donor, request, distanceKm));

            mailSender.send(message);
            log.info("Email sent to donor {} ({})", donor.getName(), donor.getEmail());

        } catch (Exception e) {
            log.error("Failed to send email to donor {}: {}", donor.getEmail(), e.getMessage());
        }
    }

    private String buildEmailBody(Donor donor, BloodRequest request, double distanceKm) {
        return String.format("""
                Dear %s,

                A patient urgently needs your help nearby!

                ─────────────────────────────
                🏥 Hospital   : %s
                🩸 Blood Type : %s
                💉 Units Needed: %d
                ⚡ Urgency    : %s
                📍 Distance   : %.1f km from you
                📝 Notes      : %s
                ─────────────────────────────

                You are registered as an available donor with blood type %s.

                Please log in to the BloodConnect app to accept or decline this request:
                👉 http://localhost:5173/donor/dashboard

                Every second counts. Thank you for saving lives. ❤️

                – BloodConnect Team
                """,
                donor.getName(),
                request.getHospital().getName(),
                request.getBloodType(),
                request.getUnitsNeeded(),
                request.getUrgencyLevel(),
                distanceKm,
                request.getNotes() != null ? request.getNotes() : "N/A",
                donor.getBloodType(),
                donor.getId()
        );
    }
}