package com.admin.catalogo.application.category.category.update;

import com.admin.catalogo.domain.category.Category;
import com.admin.catalogo.domain.category.CategoryID;

public record UpdateCategoryOutput (
        String id
) {
    public static UpdateCategoryOutput from(final Category aCategory) {
        return new UpdateCategoryOutput(aCategory.getId().getValue());
    }

    public static UpdateCategoryOutput from(final String aCategory) {
        return new UpdateCategoryOutput(aCategory);
    }
}
