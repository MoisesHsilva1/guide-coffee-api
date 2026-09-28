package com.example.guideCoffee.modules.review;

import com.example.guideCoffee.modules.review.dto.CreateReviewDTO;
import com.example.guideCoffee.modules.review.model.ReviewModel;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ReviewValidationTest {

    private static Validator validator;
    private static jakarta.validation.ValidatorFactory validatorFactory;

    @BeforeAll
    static void setUpValidator() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @AfterAll
    static void closeValidator() {
        validatorFactory.close();
    }

    @Test
    void shouldAcceptCreateReviewAtInclusiveBoundaries() {
        var review = new CreateReviewDTO("Café", null, "Descrição", "Rua 1", 1,
                null, -90.0, 180.0);

        assertThat(validator.validate(review)).isEmpty();
    }

    @Test
    void shouldRejectInvalidCreateReviewFields() {
        var review = new CreateReviewDTO(" ", "x".repeat(501), "", " ", 6,
                null, -90.1, 180.1);

        var violations = validator.validate(review);

        assertThat(violations).extracting(v -> v.getPropertyPath().toString())
                .contains("title", "imageUrl", "description", "address", "rating", "latitude", "longitude");
    }

    @Test
    void shouldRejectInvalidReviewModelFields() {
        var review = ReviewModel.builder()
                .title(" ")
                .description(" ")
                .address(" ")
                .raiting(0)
                .latitude(-90.1)
                .longitude(180.1)
                .build();

        var violations = validator.validate(review);

        assertThat(violations).extracting(v -> v.getPropertyPath().toString())
                .contains("title", "description", "address", "raiting", "latitude", "longitude");
    }
}
