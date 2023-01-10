package com.admin.catalogo.infrastructure.category;

import com.admin.catalogo.infrastructure.category.persistence.CategoryJPAEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<CategoryJPAEntity, String> {

}
