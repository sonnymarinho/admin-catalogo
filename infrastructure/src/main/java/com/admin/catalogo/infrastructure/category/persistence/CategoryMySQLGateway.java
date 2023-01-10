package com.admin.catalogo.infrastructure.category.persistence;

import com.admin.catalogo.domain.category.Category;
import com.admin.catalogo.domain.category.CategoryGateway;
import com.admin.catalogo.domain.category.CategoryID;
import com.admin.catalogo.domain.category.CategorySearchQuery;
import com.admin.catalogo.domain.pagination.Pagination;
import com.admin.catalogo.infrastructure.category.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CategoryMySQLGateway implements CategoryGateway {

    private final CategoryRepository repository;

    public CategoryMySQLGateway(CategoryRepository repository) {
        this.repository = repository;
    }

    @Override
    public Category create(Category aCategory) {
        return this.repository.save(CategoryJPAEntity.from(aCategory)).toAggregate();
    }

    @Override
    public void deleteById(CategoryID categoryID) {

    }

    @Override
    public Optional<Category> findById(CategoryID categoryID) {
        return Optional.empty();
    }

    @Override
    public Category update(Category aCategory) {
        return null;
    }

    @Override
    public Pagination<Category> findAll(CategorySearchQuery aQuery) {
        return null;
    }
}
