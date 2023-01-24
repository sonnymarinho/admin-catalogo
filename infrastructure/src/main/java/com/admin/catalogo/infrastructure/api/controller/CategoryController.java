package com.admin.catalogo.infrastructure.api.controller;

import com.admin.catalogo.application.category.category.create.CreateCategoryCommand;
import com.admin.catalogo.application.category.category.create.CreateCategoryOutput;
import com.admin.catalogo.application.category.category.create.CreateCategoryUseCase;
import com.admin.catalogo.application.category.category.delete.DeleteCategoryUseCase;
import com.admin.catalogo.application.category.category.retrieve.get.GetCategoryByIdUseCase;
import com.admin.catalogo.application.category.category.retrieve.list.ListCategoriesUseCase;
import com.admin.catalogo.application.category.category.update.UpdateCategoryCommand;
import com.admin.catalogo.application.category.category.update.UpdateCategoryOutput;
import com.admin.catalogo.application.category.category.update.UpdateCategoryUseCase;
import com.admin.catalogo.domain.category.CategorySearchQuery;
import com.admin.catalogo.domain.exceptions.Notification;
import com.admin.catalogo.domain.pagination.Pagination;
import com.admin.catalogo.infrastructure.api.CategoryAPI;
import com.admin.catalogo.infrastructure.category.models.CategoryListResponse;
import com.admin.catalogo.infrastructure.category.models.CategoryResponse;
import com.admin.catalogo.infrastructure.category.models.CreateCategoryRequest;
import com.admin.catalogo.infrastructure.category.models.UpdateCategoryRequest;
import com.admin.catalogo.infrastructure.category.presenters.CategoryAPIPresenter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

@RestController
public class CategoryController implements CategoryAPI {

    final private CreateCategoryUseCase createCategoryUseCase;
    final private GetCategoryByIdUseCase getCategoryByIdUseCase;
    private final UpdateCategoryUseCase updateCategoryUseCase;
    private final DeleteCategoryUseCase deleteCategoryUseCase;
    private final ListCategoriesUseCase listCategoriesUseCase;

    public CategoryController(final CreateCategoryUseCase createCategoryUseCase,
                              final GetCategoryByIdUseCase getCategoryByIdUseCase,
                              final UpdateCategoryUseCase updateCategoryUseCase,
                              final DeleteCategoryUseCase deleteCategoryUseCase,
                              final ListCategoriesUseCase listCategoriesUseCase
    ) {

        this.createCategoryUseCase = Objects.requireNonNull(createCategoryUseCase);
        this.getCategoryByIdUseCase = Objects.requireNonNull(getCategoryByIdUseCase);
        this.updateCategoryUseCase = Objects.requireNonNull(updateCategoryUseCase);
        this.deleteCategoryUseCase = Objects.requireNonNull(deleteCategoryUseCase);
        this.listCategoriesUseCase = Objects.requireNonNull(listCategoriesUseCase);
    }

    @Override
    public ResponseEntity<?> createCategory(CreateCategoryRequest input) {
        Boolean isActive = Optional.ofNullable(input.active()).orElse(true);

        final var anCommand = CreateCategoryCommand.with(
                input.name(),
                input.description(),
                isActive
        );

        final Function<Notification, ResponseEntity<?>> onError = notification -> {
            return ResponseEntity.unprocessableEntity().body(notification);
        };

        final Function<CreateCategoryOutput, ResponseEntity<?>> onSuccess = output -> {
            return ResponseEntity.created(URI.create("/categories/" + output.id())).body(output);
        };

        return this.createCategoryUseCase.execute(anCommand).fold(onError, onSuccess);
    }

    @Override
    public Pagination<CategoryListResponse> listCategories(String search, Integer page, Integer perPage, String sort, String dir) {
        return this.listCategoriesUseCase.execute(new CategorySearchQuery(page, perPage, search, sort, dir))
                .map(CategoryAPIPresenter::present);
    }

    @Override
    public CategoryResponse getById(String id) {
        return CategoryAPIPresenter.present.apply(this.getCategoryByIdUseCase.execute(id));
    }

    @Override
    public ResponseEntity<?> updateById(String id, UpdateCategoryRequest anInput) {
        final var anCommand = UpdateCategoryCommand
                .with(id, anInput.name(), anInput.description(), anInput.active());

        final Function<Notification, ResponseEntity<?>> onError = notification ->
                ResponseEntity.unprocessableEntity().body(notification);

        final Function<UpdateCategoryOutput, ResponseEntity<?>> onSuccess = ResponseEntity::ok;

        final var  executeResult = this.updateCategoryUseCase.execute(anCommand);
        return executeResult.fold(onError, onSuccess);
    }

    @Override
    public void deleteById(String id) {
        deleteCategoryUseCase.execute(id);
    }
}
