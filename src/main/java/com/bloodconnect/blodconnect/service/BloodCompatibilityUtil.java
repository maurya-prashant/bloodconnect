package com.bloodconnect.blodconnect.service;

import com.bloodconnect.blodconnect.enums.BloodType;
import java.util.List;
import java.util.Map;

public class BloodCompatibilityUtil {
    private static final Map<BloodType, List<BloodType>> COMPATIBILITY = Map.of(
            BloodType.A_POSITIVE,  List.of(BloodType.A_POSITIVE, BloodType.A_NEGATIVE, BloodType.O_POSITIVE, BloodType.O_NEGATIVE),
            BloodType.A_NEGATIVE,  List.of(BloodType.A_NEGATIVE, BloodType.O_NEGATIVE),
            BloodType.B_POSITIVE,  List.of(BloodType.B_POSITIVE, BloodType.B_NEGATIVE, BloodType.O_POSITIVE, BloodType.O_NEGATIVE),
            BloodType.B_NEGATIVE,  List.of(BloodType.B_NEGATIVE, BloodType.O_NEGATIVE),
            BloodType.AB_POSITIVE, List.of(BloodType.A_POSITIVE, BloodType.A_NEGATIVE, BloodType.B_POSITIVE, BloodType.B_NEGATIVE, BloodType.O_POSITIVE, BloodType.O_NEGATIVE, BloodType.AB_POSITIVE, BloodType.AB_NEGATIVE),
            BloodType.AB_NEGATIVE, List.of(BloodType.A_NEGATIVE, BloodType.B_NEGATIVE, BloodType.O_NEGATIVE, BloodType.AB_NEGATIVE),
            BloodType.O_POSITIVE,  List.of(BloodType.O_POSITIVE, BloodType.O_NEGATIVE),
            BloodType.O_NEGATIVE,  List.of(BloodType.O_NEGATIVE)
    );

    public static List<BloodType> getCompatibleDonorTypes(BloodType recipientType) {
        return COMPATIBILITY.getOrDefault(recipientType, List.of());
    }
}
