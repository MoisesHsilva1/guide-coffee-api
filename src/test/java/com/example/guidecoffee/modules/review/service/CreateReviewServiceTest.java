package com.example.guidecoffee.modules.review.service;

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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateReviewServiceTest {

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private ReviewMapper reviewMapper;

    @InjectMocks
    private CreateReviewService service;

    @Test
    void shouldMapSaveAndReturnCreatedReview() {
        var input = new CreateReviewDTO("Café Central", null, "Descrição", "Rua 1", 4,
                "Espresso", -23.5, -46.6);
        var model = ReviewModel.builder().title("Café Central").build();
        var savedModel = ReviewModel.builder().id("review-1").title("Café Central").build();
        var expected = new ReviewDTO("review-1", "Café Central", null, null, null,
                null, null, null, null, null);

        when(reviewMapper.toModel(input)).thenReturn(model);
        when(reviewRepository.save(model)).thenReturn(savedModel);
        when(reviewMapper.toDTO(savedModel)).thenReturn(expected);

        ReviewDTO result = service.execute(input);

        assertThat(result).isSameAs(expected);
        verify(reviewMapper).toModel(input);
        verify(reviewRepository).save(model);
        verify(reviewMapper).toDTO(savedModel);
    }

    @Test
    void shouldPersistExactlyTheMappedModel() {
        var input = new CreateReviewDTO("Café", null, "Descrição", "Rua 1", 3,
                null, 0.0, 0.0);
        var model = ReviewModel.builder().title("Café").build();
        var savedModel = ReviewModel.builder().id("review-1").build();

        when(reviewMapper.toModel(input)).thenReturn(model);
        when(reviewRepository.save(model)).thenReturn(savedModel);
        when(reviewMapper.toDTO(savedModel)).thenReturn(null);

        service.execute(input);

        ArgumentCaptor<ReviewModel> captor = ArgumentCaptor.forClass(ReviewModel.class);
        verify(reviewRepository).save(captor.capture());
        assertThat(captor.getValue()).isSameAs(model);
    }
}
