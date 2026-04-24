package com.amine.containerinspectionapi.dtos;


import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Generic pagination wrapper.
 * Reusable for any future paginated endpoint — not just history.
 */
@Data
public class PageResponse<T> {

    private List<T> content;
    private int     page;
    private int     size;
    private long    totalItems;
    private int     totalPages;
    private boolean last;

    public static <T> PageResponse<T> from(Page<T> page) {
        PageResponse<T> r = new PageResponse<>();
        r.content    = page.getContent();
        r.page       = page.getNumber();
        r.size       = page.getSize();
        r.totalItems = page.getTotalElements();
        r.totalPages = page.getTotalPages();
        r.last       = page.isLast();
        return r;
    }
}