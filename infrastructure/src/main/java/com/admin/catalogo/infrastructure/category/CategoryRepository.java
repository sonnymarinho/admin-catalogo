package com.admin.catalogo.infrastructure.category;

import com.admin.catalogo.infrastructure.category.persistence.CategoryJPAEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<CategoryJPAEntity, String> {
    Page<CategoryJPAEntity> findAll(Specification<CategoryJPAEntity> whereClause, Pageable page);
}
