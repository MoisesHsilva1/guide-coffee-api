package com.example.guidecoffee.shared.dto;

import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class PaginationMultipleResponseTest {

    @Test
    void shouldExposePageMetadataAndRows() {
        var page = new PageImpl<>(List.of("first", "second"), PageRequest.of(2, 5), 12);

        var response = new PaginationMultipleResponse<>(page);

        assertThat(response.success()).isTrue();
        assertThat(response.limit()).isEqualTo(5);
        assertThat(response.offset()).isEqualTo(10);
        assertThat(response.total()).isEqualTo(12);
        assertThat(response.rows()).containsExactly("first", "second");
    }
}
