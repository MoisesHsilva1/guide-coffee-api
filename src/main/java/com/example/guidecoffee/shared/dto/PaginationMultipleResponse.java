package com.example.guidecoffee.shared.dto;

import org.springframework.data.domain.Page;

import java.util.List;

public record PaginationMultipleResponse<T>(
        boolean success,
        Integer limit,
        Integer offset,
        long total,
        List<T> rows
) {
    public PaginationMultipleResponse(Page<T> page) {
        this(
                true,
                page.getSize(),
                (int) page.getPageable().getOffset(),
                page.getTotalElements(),
                page.getContent()
        );
    }
}
