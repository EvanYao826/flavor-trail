package com.flavor.trail.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageResult<T> {

    private List<T> list;
    private long total;
    private int pageNum;
    private int pageSize;
    private int pages;

    public static <T> PageResult<T> of(List<T> list, long total, int pageNum, int pageSize) {
        return PageResult.<T>builder()
                .list(list)
                .total(total)
                .pageNum(pageNum)
                .pageSize(pageSize)
                .pages((int) Math.ceil((double) total / pageSize))
                .build();
    }
}