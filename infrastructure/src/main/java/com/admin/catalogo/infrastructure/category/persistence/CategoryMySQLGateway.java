package com.admin.catalogo.infrastructure.category.persistence;

import com.admin.catalogo.domain.category.Category;
import com.admin.catalogo.domain.category.CategoryGateway;
import com.admin.catalogo.domain.category.CategoryID;
import com.admin.catalogo.domain.category.CategorySearchQuery;
import com.admin.catalogo.domain.pagination.Pagination;
import com.admin.catalogo.infrastructure.category.CategoryRepository;
import com.admin.catalogo.infrastructure.utils.SpecificationUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.*;
import java.util.Optional;

import static com.admin.catalogo.infrastructure.utils.SpecificationUtils.like;

@Service
public class CategoryMySQLGateway implements CategoryGateway {

    private final CategoryRepository repository;

    public CategoryMySQLGateway(CategoryRepository repository) {
        this.repository = repository;
    }

    @Override
    public Category create(Category aCategory) {
        return save(aCategory);
    }

    private Category save(final Category aCategory) {
        return this.repository.save(CategoryJPAEntity.from(aCategory)).toAggregate();
    }

    @Override
    public void deleteById(CategoryID categoryID) {
        String categoryIDValue = categoryID.getValue();

        boolean entityExists = this.repository.existsById(categoryIDValue);
        if (entityExists) repository.deleteById(categoryIDValue);
    }

    @Override
    public Optional<Category> findById(CategoryID categoryID) {
        return repository.findById(categoryID.getValue())
                .map(entity -> entity.toAggregate());
    }

    @Override
    public Category update(final Category aCategory) {
        return save(aCategory);
    }

    @Override
    public Pagination<Category> findAll(CategorySearchQuery aQuery) {
        //Paginação
        final var page = PageRequest.of(
                aQuery.page(),
                aQuery.perPage(),
                Sort.by(Sort.Direction.fromString(aQuery.direction()), aQuery.sort())
        );

        //Busca dinamica pelo criterio terms (name ou description)
        final var specifications = Optional.ofNullable(aQuery.categoryTerms())
                .filter(terms -> !terms.isBlank() && !terms.isEmpty())
                .map(term -> {
                    Specification<CategoryJPAEntity> nameLike = like("name", term);
                    Specification<CategoryJPAEntity> descriptionLike = like("description", term);
                    return nameLike.or(descriptionLike);
                })
                .orElse(null);

        final var pageResult = this.repository.findAll(Specification.where(specifications), page);

        return new Pagination<>(
                pageResult.getNumber(),
                pageResult.getSize(),
                pageResult.getTotalElements(),
                pageResult.map(entity -> entity.toAggregate()).toList()
        );
    }
}
