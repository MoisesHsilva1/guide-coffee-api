package com.example.guideCoffee.modules.review.controller;

import com.example.guideCoffee.modules.review.dto.CreateReviewDTO;
import com.example.guideCoffee.modules.review.dto.ReviewDTO;
import com.example.guideCoffee.modules.review.service.CreateReviewService;
import com.example.guideCoffee.modules.review.service.FindAllReviewService;
import com.example.guideCoffee.modules.review.service.FindOneReviewService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReviewControllerTest {

    @Mock
    private CreateReviewService createReviewService;

    @Mock
    private FindAllReviewService findAllReviewService;

    @Mock
    private FindOneReviewService findOneReviewService;

    private ReviewController controller;

    @BeforeEach
    void setUp() {
        controller = new ReviewController(createReviewService, findAllReviewService, findOneReviewService);
    }

    @Test
    void shouldCreateReviewWithCreatedStatus() {
        var input = new CreateReviewDTO("Café", null, "Descrição", "Rua 1", 4,
                null, 0.0, 0.0);
        var expected = new ReviewDTO("1", "Café", null, "Descrição", "Rua 1", 4,
                0.0, 0.0, null, null);
        when(createReviewService.execute(input)).thenReturn(expected);

        var response = controller.create(input);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isSameAs(expected);
        verify(createReviewService).execute(input);
    }

    @Test
    void shouldReturnPaginatedReviewsWithOkStatus() {
        var page = new PageImpl<>(List.of(new ReviewDTO("1", "Café", null, null,
                null, 4, 0.0, 0.0, null, null)), PageRequest.of(2, 5), 11);
        when(findAllReviewService.execute(2, 5)).thenReturn(page);

        var response = controller.findAll(2, 5);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().success()).isTrue();
        assertThat(response.getBody().rows()).hasSize(1);
        verify(findAllReviewService).execute(2, 5);
    }

    @Test
    void shouldReturnReviewByIdWithOkStatus() {
        var expected = new ReviewDTO("review-1", "Café", null, null, null,
                4, 0.0, 0.0, null, null);
        when(findOneReviewService.execute("review-1")).thenReturn(expected);

        var response = controller.findOne("review-1");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isSameAs(expected);
        verify(findOneReviewService).execute("review-1");
    }
}
