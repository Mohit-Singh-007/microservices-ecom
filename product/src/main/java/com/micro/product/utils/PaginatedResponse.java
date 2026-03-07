package com.micro.product.utils;

import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;
@Data
public class PaginatedResponse<T> {

    private List<T> data;
    private int page;
    private int size;
    private long totalElements;
    private int totalPages;

    public PaginatedResponse(List<T> data, Page<?> pageObj) {
        this.data = data;
        this.page = pageObj.getNumber();
        this.size = pageObj.getSize();
        this.totalElements = pageObj.getTotalElements();
        this.totalPages = pageObj.getTotalPages();
    }
}

