package com.admin.catalogo.infrastructure.api.controller;

import com.admin.catalogo.application.category.category.create.CreateCategoryCommand;
import com.admin.catalogo.application.category.category.create.CreateCategoryOutput;
import com.admin.catalogo.application.category.category.create.CreateCategoryUseCase;
import com.admin.catalogo.domain.exceptions.Notification;
import com.admin.catalogo.domain.pagination.Pagination;
import com.admin.catalogo.infrastructure.api.CategoryAPI;
import com.admin.catalogo.infrastructure.category.models.CreateCategoryAPIInput;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

@RestController
public class CategoryController implements CategoryAPI {

    final private CreateCategoryUseCase createCategoryUseCase;

    public CategoryController(final CreateCategoryUseCase createCategoryUseCase) {
        this.createCategoryUseCase = Objects.requireNonNull(createCategoryUseCase);
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
}
