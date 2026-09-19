package com.example.guideCoffee.modules.review.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateReviewDTO(
        String title,
        String imageUrl,
        String description,
        String address,
        Integer rating,
        String drinkType,
        Double latitude,
        Double longitude
) {
}
