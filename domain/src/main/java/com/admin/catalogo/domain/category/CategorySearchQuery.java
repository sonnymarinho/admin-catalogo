package com.admin.catalogo.domain.category;

public record CategorySearchQuery(
        int page,
        int perPage,
        String categoryTerms,
        String sort,
        String direction
) {
}
