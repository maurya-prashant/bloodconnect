package com.bloodconnect.blodconnect.dto;
import com.bloodconnect.blodconnect.enums.DonorResponseStatus;
import lombok.Data;

@Data
public class DonorResponseDTO {
    private Long bloodRequestId;
    private DonorResponseStatus donorResponseStatus;
}
