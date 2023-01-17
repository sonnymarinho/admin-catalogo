package com.admin.catalogo.infrastructure.configuration.usecases;

import com.admin.catalogo.application.category.category.create.CreateCategoryUseCase;
import com.admin.catalogo.application.category.category.create.DefaultCreateCategoryUseCase;
import com.admin.catalogo.application.category.category.delete.DefaultDeleteCategoryUseCase;
import com.admin.catalogo.application.category.category.delete.DeleteCategoryUseCase;
import com.admin.catalogo.application.category.category.retrieve.get.DefaultGetCategoryByIdUseCase;
import com.admin.catalogo.application.category.category.retrieve.get.GetCategoryByIdUseCase;
import com.admin.catalogo.application.category.category.retrieve.list.DefaultListCategoriesUseCase;
import com.admin.catalogo.application.category.category.retrieve.list.ListCategoriesUseCase;
import com.admin.catalogo.application.category.category.update.DefaultUpdateCategoryUseCase;
import com.admin.catalogo.application.category.category.update.UpdateCategoryUseCase;
import com.admin.catalogo.domain.category.CategoryGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CategoryUseCaseConfig {
    private final CategoryGateway categoryGateway;


    public CategoryUseCaseConfig(CategoryGateway categoryGateway) {
        this.categoryGateway = categoryGateway;
    }

    @Bean
    public CreateCategoryUseCase createCategoryUseCase() {
        return new DefaultCreateCategoryUseCase(categoryGateway);
    }

    @Bean
    public UpdateCategoryUseCase updateCategoryUseCase() {
        return new DefaultUpdateCategoryUseCase(categoryGateway);
    }

    @Bean
    public GetCategoryByIdUseCase getCategoryByIdUseCase() {
        return new DefaultGetCategoryByIdUseCase(categoryGateway);
    }

    @Bean
    public ListCategoriesUseCase listCategoriesUseCase() {
        return new DefaultListCategoriesUseCase(categoryGateway);
    }

    @Bean
    public DeleteCategoryUseCase deleteCategoryUseCase() {
        return new DefaultDeleteCategoryUseCase(categoryGateway);
    }
}
