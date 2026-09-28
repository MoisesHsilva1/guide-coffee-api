package com.example.guideCoffee.modules.review.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateReviewDTO(
        @NotBlank
        @Size(max = 120)
        String title,
        @Size(max = 500)
        String imageUrl,
        @NotBlank
        @Size(max = 2000)
        String description,
        @NotBlank
        @Size(max = 120)
        String address,
        @NotNull
        @Min(1)
        @Max(5)
        Integer rating,
        String drinkType,
        @NotNull
        @DecimalMin("-90.0")
        @DecimalMax("90.0")
        Double latitude,
        @NotNull
        @DecimalMin("-180.0")
        @DecimalMax("180.0")
        Double longitude
) {
}
