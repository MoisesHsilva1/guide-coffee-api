package com.example.guidecoffee.modules.review.service;

import com.example.guidecoffee.modules.filestorage.dto.UploadedImage;
import com.example.guidecoffee.modules.filestorage.strategy.FileStorageStrategy;
import com.example.guidecoffee.modules.review.dto.CreateReviewDTO;
import com.example.guidecoffee.modules.review.dto.ReviewDTO;
import com.example.guidecoffee.modules.review.mapper.ReviewMapper;
import com.example.guidecoffee.modules.review.model.ReviewModel;
import com.example.guidecoffee.modules.review.repository.ReviewRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateReviewServiceTest {

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private ReviewMapper reviewMapper;

    @Mock
    private FileStorageStrategy fileStorageStrategy;

    @InjectMocks
    private CreateReviewService service;

    @Test
    void shouldMapSaveAndReturnCreatedReview() throws Exception {
        var image = new MockMultipartFile("image", "coffee.jpg", "image/jpeg", "image".getBytes());
        var input = new CreateReviewDTO("Café Central", image, "Descrição", "Rua 1", 4, "Espresso");
        var model = ReviewModel.builder().title("Café Central").build();
        var savedModel = ReviewModel.builder().id("review-1").title("Café Central").build();
        var expected = new ReviewDTO("review-1", "Café Central", null, null, null, null, null, null);

        when(fileStorageStrategy.upload(image)).thenReturn(new UploadedImage(
                "https://example.com/coffee.jpg", "guide-coffee/reviews/coffee"
        ));
        when(reviewMapper.toModel(input, "https://example.com/coffee.jpg")).thenReturn(model);
        when(reviewRepository.save(model)).thenReturn(savedModel);
        when(reviewMapper.toDTO(savedModel)).thenReturn(expected);

        ReviewDTO result = service.execute(input);

        assertThat(result).isSameAs(expected);
        verify(fileStorageStrategy).upload(image);
        verify(reviewMapper).toModel(input, "https://example.com/coffee.jpg");
        verify(reviewRepository).save(model);
        verify(reviewMapper).toDTO(savedModel);
    }

    @Test
    void shouldPersistExactlyTheMappedModel() throws Exception {
        var input = new CreateReviewDTO("Café", null, "Descrição", "Rua 1", 3, null);
        var model = ReviewModel.builder().title("Café").build();
        var savedModel = ReviewModel.builder().id("review-1").build();

        when(reviewMapper.toModel(input, null)).thenReturn(model);
        when(reviewRepository.save(model)).thenReturn(savedModel);
        when(reviewMapper.toDTO(savedModel)).thenReturn(null);

        service.execute(input);

        ArgumentCaptor<ReviewModel> captor = ArgumentCaptor.forClass(ReviewModel.class);
        verify(reviewRepository).save(captor.capture());
        assertThat(captor.getValue()).isSameAs(model);
    }

    @Test
    void shouldNotPersistReviewWhenImageUploadFails() throws Exception {
        var image = new MockMultipartFile("image", "coffee.jpg", "image/jpeg", "image".getBytes());
        var input = new CreateReviewDTO("Café", image, "Descrição", "Rua 1", 3, null);
        when(fileStorageStrategy.upload(image)).thenThrow(new IOException("Cloudinary unavailable"));

        assertThatThrownBy(() -> service.execute(input))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Unable to upload review image")
                .hasCauseInstanceOf(IOException.class);

        verify(reviewRepository, never()).save(any());
    }
}
