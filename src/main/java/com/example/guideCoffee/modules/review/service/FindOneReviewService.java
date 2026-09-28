package com.example.guideCoffee.modules.review.service;

import com.example.guideCoffee.modules.review.dto.ReviewDTO;
import com.example.guideCoffee.modules.review.mapper.ReviewMapper;
import com.example.guideCoffee.modules.review.repository.ReviewRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class FindOneReviewService {
    private final ReviewRepository reviewRepository;
    private final ReviewMapper reviewMapper;

    public ReviewDTO execute(String id) throws RuntimeException {
        return reviewRepository.findById(id)
                .map(reviewMapper::toDTO)
                .orElseThrow(() -> new RuntimeException("Review not found"));
    }
}
