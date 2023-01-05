package com.admin.catalogo.domain.pagination;

import java.util.List;
import java.util.function.Function;

public record Pagination<T>(
        int currentPage,
        int perPage,
        long total,
        List<T> items
) {
    public <R> Pagination<R> map(Function<T, R> mapper) {
        List<R> aNewList = this.items.parallelStream().map(mapper).toList();

        return new Pagination<>(currentPage(), perPage(), total(), aNewList);
    }
}
