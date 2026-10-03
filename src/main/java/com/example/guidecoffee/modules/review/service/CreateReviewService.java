package com.example.guidecoffee.modules.review.service;

import com.example.guidecoffee.modules.review.dto.CreateReviewDTO;
import com.example.guidecoffee.modules.review.dto.ReviewDTO;
import com.example.guidecoffee.modules.review.mapper.ReviewMapper;
import com.example.guidecoffee.modules.review.model.ReviewModel;
import com.example.guidecoffee.modules.review.repository.ReviewRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class CreateReviewService {
    private final ReviewRepository reviewRepository;
    private final ReviewMapper reviewMapper;

    @Transactional
    public ReviewDTO execute(CreateReviewDTO review) {
        ReviewModel reviewModel = reviewMapper.toModel(review);
        ReviewModel savedReview = reviewRepository.save(reviewModel);

        return reviewMapper.toDTO(savedReview);
    }
}

