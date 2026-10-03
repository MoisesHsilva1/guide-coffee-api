package com.example.guidecoffee.modules.review.controller;

import com.example.guidecoffee.modules.review.dto.CreateReviewDTO;
import com.example.guidecoffee.modules.review.dto.ReviewDTO;
import com.example.guidecoffee.modules.review.service.CreateReviewService;
import com.example.guidecoffee.modules.review.service.FindAllReviewService;
import com.example.guidecoffee.modules.review.service.FindOneReviewService;
import com.example.guidecoffee.shared.dto.PaginationMultipleResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/reviews")
@RequiredArgsConstructor
@Tag(name = "Reviews", description = "Avaliações de cafeterias")
public class ReviewController {
    private final CreateReviewService createReviewService;
    private final FindAllReviewService findAllReviewService;
    private final FindOneReviewService findOneReviewService;

    @PostMapping
    @Operation(summary = "Criar avaliação")
    public ResponseEntity<ReviewDTO> create(@RequestBody @Valid CreateReviewDTO review) {
        return ResponseEntity.status(HttpStatus.CREATED).body(createReviewService.execute(review));
    }

    @GetMapping
    @Operation(summary = "Listar avaliações")
    public ResponseEntity<PaginationMultipleResponse<ReviewDTO>> findAll(@RequestParam(defaultValue = "0") Integer offset, @RequestParam(defaultValue = "10") Integer limit) {
        var page = findAllReviewService.execute(offset, limit);
        return ResponseEntity.ok(new PaginationMultipleResponse<>(page));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar avaliação por ID")
    public ResponseEntity<ReviewDTO> findOne(@PathVariable String id) {
        return ResponseEntity.ok(findOneReviewService.execute(id));
    }
}
