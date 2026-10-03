package com.example.guidecoffee.modules.review.service;

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
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FindAllReviewServiceTest {

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private ReviewMapper reviewMapper;

    @InjectMocks
    private FindAllReviewService service;

    @Test
    void shouldFetchPageAndMapEveryReview() {
        var first = ReviewModel.builder().id("1").title("First").build();
        var second = ReviewModel.builder().id("2").title("Second").build();
        var firstDto = new ReviewDTO("1", "First", null, null, null, null, null, null, null, null);
        var secondDto = new ReviewDTO("2", "Second", null, null, null, null, null, null, null, null);
        var page = new PageImpl<>(List.of(first, second));

        when(reviewRepository.findAll(org.mockito.ArgumentMatchers.any(Pageable.class))).thenReturn(page);
        when(reviewMapper.toDTO(first)).thenReturn(firstDto);
        when(reviewMapper.toDTO(second)).thenReturn(secondDto);

        var result = service.execute(2, 5);

        assertThat(result.getContent()).containsExactly(firstDto, secondDto);
        assertThat(result.getTotalElements()).isEqualTo(2);

        ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
        verify(reviewRepository).findAll(pageableCaptor.capture());
        assertThat(pageableCaptor.getValue().getPageNumber()).isEqualTo(2);
        assertThat(pageableCaptor.getValue().getPageSize()).isEqualTo(5);
    }
}
