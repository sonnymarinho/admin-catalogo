package com.admin.catalogo.infrastructure.api.controller;

import com.admin.catalogo.application.category.category.create.CreateCategoryCommand;
import com.admin.catalogo.application.category.category.create.CreateCategoryOutput;
import com.admin.catalogo.application.category.category.create.CreateCategoryUseCase;
import com.admin.catalogo.application.category.category.delete.DeleteCategoryUseCase;
import com.admin.catalogo.application.category.category.retrieve.get.GetCategoryByIdUseCase;
import com.admin.catalogo.application.category.category.update.UpdateCategoryCommand;
import com.admin.catalogo.application.category.category.update.UpdateCategoryOutput;
import com.admin.catalogo.application.category.category.update.UpdateCategoryUseCase;
import com.admin.catalogo.domain.exceptions.Notification;
import com.admin.catalogo.domain.pagination.Pagination;
import com.admin.catalogo.infrastructure.api.CategoryAPI;
import com.admin.catalogo.infrastructure.category.models.CategoryAPIOutput;
import com.admin.catalogo.infrastructure.category.models.CreateCategoryAPIInput;
import com.admin.catalogo.infrastructure.category.models.UpdateCategoryAPIInput;
import com.admin.catalogo.infrastructure.category.presenters.CategoryAPIPresenter;
import io.vavr.control.Either;
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

    public CategoryController(final CreateCategoryUseCase createCategoryUseCase,
                              final GetCategoryByIdUseCase getCategoryByIdUseCase,
                              final UpdateCategoryUseCase updateCategoryUseCase,
                              final DeleteCategoryUseCase deleteCategoryUseCase
    ) {

        this.createCategoryUseCase = Objects.requireNonNull(createCategoryUseCase);
        this.getCategoryByIdUseCase = Objects.requireNonNull(getCategoryByIdUseCase);
        this.updateCategoryUseCase = Objects.requireNonNull(updateCategoryUseCase);
        this.deleteCategoryUseCase = Objects.requireNonNull(deleteCategoryUseCase);
    }

    @Override
    public ResponseEntity<?> createCategory(CreateCategoryAPIInput input) {
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
    public Pagination<?> listCategories(String search, Integer page, Integer perPage, String sort, String dir) {
        return null;
    }

    @Override
    public CategoryAPIOutput getById(String id) {
        return CategoryAPIPresenter.present.apply(this.getCategoryByIdUseCase.execute(id));
    }

    @Override
    public ResponseEntity<?> updateById(String id, UpdateCategoryAPIInput anInput) {
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
