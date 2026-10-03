package com.example.guidecoffee.modules.review.service;

import com.example.guidecoffee.modules.review.dto.ReviewDTO;
import com.example.guidecoffee.modules.review.mapper.ReviewMapper;
import com.example.guidecoffee.modules.review.model.ReviewModel;
import com.example.guidecoffee.modules.review.repository.ReviewRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FindOneReviewServiceTest {

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private ReviewMapper reviewMapper;

    @InjectMocks
    private FindOneReviewService service;

    @Test
    void shouldReturnMappedReviewWhenIdExists() {
        var model = ReviewModel.builder().id("review-1").title("Café").build();
        var expected = new ReviewDTO("review-1", "Café", null, null, null, null, null, null, null, null);
        when(reviewRepository.findById("review-1")).thenReturn(Optional.of(model));
        when(reviewMapper.toDTO(model)).thenReturn(expected);

        ReviewDTO result = service.execute("review-1");

        assertThat(result).isSameAs(expected);
        verify(reviewRepository).findById("review-1");
        verify(reviewMapper).toDTO(model);
    }

    @Test
    void shouldThrowWhenIdDoesNotExist() {
        when(reviewRepository.findById("missing")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.execute("missing"))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Review not found");

        verify(reviewRepository).findById("missing");
        verifyNoInteractions(reviewMapper);
    }
}
