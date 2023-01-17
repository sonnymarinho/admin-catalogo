package com.admin.catalogo.application.category.category.retrieve.list;

import com.admin.catalogo.application.category.UseCase;
import com.admin.catalogo.domain.category.CategorySearchQuery;
import com.admin.catalogo.domain.pagination.Pagination;

public abstract class ListCategoriesUseCase extends UseCase<CategorySearchQuery, Pagination<CategoryListOutput>> {
}
