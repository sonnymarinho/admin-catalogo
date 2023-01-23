package com.admin.catalogo.infrastructure.category.presenters;

import com.admin.catalogo.application.category.category.retrieve.get.CategoryOutput;
import com.admin.catalogo.infrastructure.category.models.CategoryAPIOutput;

import java.util.function.Function;

public interface CategoryAPIPresenter {

    Function<CategoryOutput, CategoryAPIOutput> present =
            categoryOutput -> new CategoryAPIOutput(
                categoryOutput.id().getValue(),
                categoryOutput.name(),
                categoryOutput.description(),
                categoryOutput.isActive(),
                categoryOutput.createdAt(),
                categoryOutput.updatedAt(),
                categoryOutput.deletedAt()
        );
}
