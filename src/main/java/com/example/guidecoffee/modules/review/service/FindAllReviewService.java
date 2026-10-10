package com.example.guidecoffee.modules.review.service;

import com.example.guidecoffee.modules.review.dto.ReviewDTO;
import com.example.guidecoffee.modules.review.mapper.ReviewMapper;
import com.example.guidecoffee.modules.review.model.ReviewModel;
import com.example.guidecoffee.modules.review.repository.ReviewRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class FindAllReviewService {
    private static final int MAX_PAGE_OFFSET = 10_000;
    private static final int MAX_PAGE_SIZE = 100;

    private final ReviewRepository reviewRepository;
    private final ReviewMapper reviewMapper;

    public Page<ReviewDTO> execute(Integer offset, Integer limit) {
        if (offset == null || offset < 0 || offset > MAX_PAGE_OFFSET) {
            throw new IllegalArgumentException(
                    "offset must be between 0 and " + MAX_PAGE_OFFSET
            );
        }
        if (limit == null || limit < 1 || limit > MAX_PAGE_SIZE) {
            throw new IllegalArgumentException("limit must be between 1 and " + MAX_PAGE_SIZE);
        }

        Pageable pageable = PageRequest.of(offset, limit);

        return fetchReviews(pageable).map(reviewMapper::toDTO);
    }

    private Page<ReviewModel> fetchReviews(Pageable pageable) {
        return reviewRepository.findAll(pageable);
    }
}
