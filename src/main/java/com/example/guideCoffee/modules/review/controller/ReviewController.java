package com.example.guideCoffee.modules.review.controller;

import com.example.guideCoffee.modules.review.dto.CreateReviewDTO;
import com.example.guideCoffee.modules.review.dto.ReviewDTO;
import com.example.guideCoffee.modules.review.service.CreateReviewService;
import com.example.guideCoffee.modules.review.service.FindAllReviewService;
import com.example.guideCoffee.shared.dto.PaginationMultipleResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/reviews")
@RequiredArgsConstructor
public class ReviewController {
    private final CreateReviewService createReviewService;
    private final FindAllReviewService findAllReviewService;

    @PostMapping()
    public ResponseEntity<ReviewDTO> create(@RequestBody @Valid CreateReviewDTO review) {
        return ResponseEntity.status(HttpStatus.CREATED).body(createReviewService.execute(review));
    }

    @GetMapping()
    public ResponseEntity<PaginationMultipleResponse<ReviewDTO>> findAll(
            @RequestParam(defaultValue = "0") Integer offset,
            @RequestParam(defaultValue = "10") Integer limit
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(new PaginationMultipleResponse<>(findAllReviewService.execute(offset, limit)));
    }
}
