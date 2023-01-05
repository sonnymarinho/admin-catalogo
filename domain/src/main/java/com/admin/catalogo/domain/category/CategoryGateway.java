package com.admin.catalogo.domain.category;

import com.admin.catalogo.domain.pagination.Pagination;

import java.util.Optional;

public interface CategoryGateway {

    Category create(Category aCategory);

    void deleteById(CategoryID categoryID);

    Optional<Category> findById(CategoryID categoryID);

    Category update(Category aCategory);

    Pagination<Category> findAll(CategorySearchQuery aQuery);
}
