package com.example.guidecoffee.modules.review.service;

import com.example.guidecoffee.modules.filestorage.strategy.FileStorageStrategy;
import com.example.guidecoffee.modules.filestorage.dto.UploadedImage;
import com.example.guidecoffee.modules.review.dto.CreateReviewDTO;
import com.example.guidecoffee.modules.review.dto.ReviewDTO;
import com.example.guidecoffee.modules.review.mapper.ReviewMapper;
import com.example.guidecoffee.modules.review.model.ReviewModel;
import com.example.guidecoffee.modules.review.repository.ReviewRepository;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@AllArgsConstructor
public class CreateReviewService {
    private final ReviewRepository reviewRepository;
    private final FileStorageStrategy fileStorageStrategy;
    private final ReviewMapper reviewMapper;

    @Transactional
    public ReviewDTO execute(@Valid CreateReviewDTO review) {
        UploadedImage uploadedImage = uploadImage(review.imageUrl());
        String imageUrl = uploadedImage == null ? null : uploadedImage.secureUrl();

        ReviewModel reviewModel = reviewMapper.toModel(review, imageUrl);
        ReviewModel savedReview = reviewRepository.save(reviewModel);

        return reviewMapper.toDTO(savedReview);
    }

    private UploadedImage uploadImage(MultipartFile image) {
        try {
            return fileStorageStrategy.upload(image);
        } catch (IOException exception) {
            throw new IllegalStateException("Unable to upload review image", exception);
        }
    }
}

