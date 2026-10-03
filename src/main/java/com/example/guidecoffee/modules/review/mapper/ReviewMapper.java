package com.example.guidecoffee.modules.review.mapper;

import com.example.guidecoffee.modules.review.dto.CreateReviewDTO;
import com.example.guidecoffee.modules.review.dto.ReviewDTO;
import com.example.guidecoffee.modules.review.model.ReviewModel;
import org.springframework.stereotype.Component;

@Component
public class ReviewMapper {

    public ReviewModel toModel(CreateReviewDTO dto) {
        if (dto == null) {
            return null;
        }

        return ReviewModel.builder()
                .title(dto.title())
                .imageUrl(dto.imageUrl())
                .description(dto.description())
                .address(dto.address())
                .latitude(dto.latitude())
                .longitude(dto.longitude())
                .raiting(dto.rating())
                .drinkType(dto.drinkType())
                .build();
    }

    public ReviewDTO toDTO(ReviewModel model) {
        if (model == null) {
            return null;
        }

        return new ReviewDTO(
                model.getId(),
                model.getTitle(),
                model.getImageUrl(),
                model.getDescription(),
                model.getAddress(),
                model.getRaiting(),
                model.getLatitude(),
                model.getLongitude(),
                model.getDrinkType(),
                model.getCreatedAt()
        );
    }
}

