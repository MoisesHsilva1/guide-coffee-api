package com.example.guidecoffee.modules.review.mapper;

import com.example.guidecoffee.modules.review.dto.CreateReviewDTO;
import com.example.guidecoffee.modules.review.dto.ReviewDTO;
import com.example.guidecoffee.modules.review.model.ReviewModel;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

class ReviewMapperTest {

    private final ReviewMapper mapper = new ReviewMapper();

    @Test
    void shouldMapCreateDtoToModel() {
        var input = new CreateReviewDTO(
                "Café Central",
                new MockMultipartFile("imageUrl", "cafe.jpg", "image/jpeg", "image".getBytes()),
                "Café especial e atendimento cordial",
                "Rua das Flores, 10",
                5,
                "Espresso"
        );

        ReviewModel result = mapper.toModel(input);

        assertThat(result.getId()).isNull();
        assertThat(result.getTitle()).isEqualTo(input.title());
        assertThat(result.getImageUrl()).isNull();
        assertThat(result.getDescription()).isEqualTo(input.description());
        assertThat(result.getAddress()).isEqualTo(input.address());
        assertThat(result.getRaiting()).isEqualTo(input.rating());
        assertThat(result.getDrinkType()).isEqualTo(input.drinkType());
        assertThat(result.getCreatedAt()).isNull();
    }

    @Test
    void shouldMapProvidedImageUrlToModel() {
        var input = new CreateReviewDTO("Café Central", null, "Descrição", "Rua 1", 5, "Espresso");

        ReviewModel result = mapper.toModel(input, "https://example.com/cafe.jpg");

        assertThat(result.getImageUrl()).isEqualTo("https://example.com/cafe.jpg");
    }

    @Test
    void shouldMapModelToDto() {
        Instant createdAt = Instant.parse("2026-01-01T12:00:00Z");
        ReviewModel model = ReviewModel.builder()
                .id("review-1")
                .title("Café Central")
                .imageUrl("https://example.com/cafe.jpg")
                .description("Café especial")
                .address("Rua das Flores, 10")
                .raiting(5)
                .drinkType("Espresso")
                .createdAt(createdAt)
                .build();

        ReviewDTO result = mapper.toDTO(model);

        assertThat(result).isEqualTo(new ReviewDTO(
                "review-1", "Café Central", "https://example.com/cafe.jpg",
                "Café especial", "Rua das Flores, 10", 5,
                "Espresso", createdAt
        ));
    }

    @Test
    void shouldReturnNullWhenMappingNullValues() {
        assertThat(mapper.toModel(null)).isNull();
        assertThat(mapper.toDTO(null)).isNull();
    }
}
