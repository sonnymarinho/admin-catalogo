package com.admin.catalogo.application.category.category.create;

import com.admin.catalogo.domain.category.Category;
import com.admin.catalogo.domain.category.CategoryID;

public record CreateCategoryOutput(
        String id
) {
    public static CreateCategoryOutput from(final Category aCategory) {
        return new CreateCategoryOutput(aCategory.getId().getValue());
    }

    public static CreateCategoryOutput from(CategoryID anId) {
        return new CreateCategoryOutput(anId.getValue());
    }
}
