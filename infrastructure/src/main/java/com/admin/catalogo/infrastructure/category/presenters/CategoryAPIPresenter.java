package com.admin.catalogo.infrastructure.category.presenters;

import com.admin.catalogo.application.category.category.retrieve.get.CategoryOutput;
import com.admin.catalogo.application.category.category.retrieve.list.CategoryListOutput;
import com.admin.catalogo.infrastructure.category.models.CategoryResponse;
import com.admin.catalogo.infrastructure.category.models.CategoryListResponse;

import java.util.function.Function;

public interface CategoryAPIPresenter {

    Function<CategoryOutput, CategoryResponse> present =
            categoryOutput -> new CategoryResponse(
                categoryOutput.id().getValue(),
                categoryOutput.name(),
                categoryOutput.description(),
                categoryOutput.isActive(),
                categoryOutput.createdAt(),
                categoryOutput.updatedAt(),
                categoryOutput.deletedAt()
        );

    static CategoryListResponse present(CategoryListOutput categoryOutput) {
        return new CategoryListResponse(
                categoryOutput.id().getValue(),
                categoryOutput.name(),
                categoryOutput.description(),
                categoryOutput.isActive(),
                categoryOutput.createdAt(),
                categoryOutput.updatedAt(),
                categoryOutput.deletedAt()
        );
    }
}
