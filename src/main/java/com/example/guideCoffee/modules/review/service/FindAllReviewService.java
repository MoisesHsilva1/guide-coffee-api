package com.example.guideCoffee.modules.review.service;

import com.example.guideCoffee.modules.review.dto.ReviewDTO;
import com.example.guideCoffee.modules.review.mapper.ReviewMapper;
import com.example.guideCoffee.modules.review.model.ReviewModel;
import com.example.guideCoffee.modules.review.repository.ReviewRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class FindAllReviewService {
    private final ReviewRepository reviewRepository;
    private final ReviewMapper reviewMapper;

    public Page<ReviewDTO> execute(Integer offset, Integer limit) {
        Pageable pageable = PageRequest.of(offset, limit);

        return fetchReviews(pageable).map(reviewMapper::toDTO);
    }

    private Page<ReviewModel> fetchReviews(Pageable pageable) {
        return reviewRepository.findAll(pageable);
    }
}
