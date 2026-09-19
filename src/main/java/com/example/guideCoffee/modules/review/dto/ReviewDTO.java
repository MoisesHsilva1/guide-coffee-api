package com.example.guideCoffee.modules.review.dto;

import java.time.Instant;

public record ReviewDTO(
        String id,
        String title,
        String imageUrl,
        String description,
        String address,
        Integer rating,
        Double latitude,
        Double longitude,
        String drinkType,
        Instant createdAt
) {
}
