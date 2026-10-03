package com.example.guidecoffee.modules.review.model;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document(collection = "reviews")
@CompoundIndex(name = "idx_location_stars", def = "{'location': 1, 'stars': -1}")
@AllArgsConstructor
@Builder
@Getter
@EqualsAndHashCode(of = "id")
public class ReviewModel {
    @Id
    private String id;

    @NotBlank
    @Size(max = 120)
    @Indexed
    private String title;

    private String drinkType;

    @NotNull
    @DecimalMin("-90.0")
    @DecimalMax("90.0")
    private Double latitude;

    @NotNull
    @DecimalMin("-180.0")
    @DecimalMax("180.0")
    private Double longitude;

    @NotNull
    @Min(1)
    @Max(5)
    @Indexed
    private Integer raiting;

    @NotBlank
    @Size(max = 2000)
    private String description;

    @NotBlank
    @Size(max = 120)
    @Indexed
    private String address;

    @Size(max = 500)
    private String imageUrl;

    @CreatedDate
    private Instant createdAt;
}