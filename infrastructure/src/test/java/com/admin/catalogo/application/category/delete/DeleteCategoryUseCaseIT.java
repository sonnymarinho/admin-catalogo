package com.admin.catalogo.application.category.delete;

import com.admin.catalogo.IntegrationTest;
import com.admin.catalogo.application.category.category.delete.DeleteCategoryUseCase;
import com.admin.catalogo.domain.category.Category;
import com.admin.catalogo.domain.category.CategoryGateway;
import com.admin.catalogo.domain.category.CategoryID;
import com.admin.catalogo.infrastructure.category.CategoryRepository;
import com.admin.catalogo.infrastructure.category.persistence.CategoryJPAEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@IntegrationTest
public class DeleteCategoryUseCaseIT {

    @Autowired
    private DeleteCategoryUseCase useCase;

    @Autowired
    private CategoryRepository categoryRepository;

    @SpyBean
    private CategoryGateway categoryGateway;

    @Test
    void givenAValidId_whenCallsDeleteCategory_shouldBeOK() {
        final var aCategory = Category.newCategory("Filmes", "A categoria mais assistida");
        CategoryID expectedID = aCategory.getId();

        save(aCategory);
        Assertions.assertEquals(1, categoryRepository.count());

        Assertions.assertDoesNotThrow(() -> useCase.execute(expectedID.getValue()));

        Assertions.assertEquals(0, categoryRepository.count());

        Mockito.verify(categoryGateway, times(1)).deleteById(eq(expectedID));
    }

    @Test
    void givenAnInvalidId_whenCallsDeleteCategory_shouldBeOK() {
        CategoryID expectedID = CategoryID.from("123");

        Assertions.assertEquals(0, categoryRepository.count());

        Assertions.assertDoesNotThrow(() -> useCase.execute(expectedID.getValue()));

        Assertions.assertEquals(0, categoryRepository.count());

        Mockito.verify(categoryGateway, times(1)).deleteById(eq(expectedID));
    }

    @Test
    void givenAValidId_whenGatewayThrowsException_shouldReturnException() {
        final var aCategory = Category.newCategory("Filmes", "A categoria mais assistida");

        CategoryID expectedID = aCategory.getId();

        doThrow(new IllegalStateException("Gateway error"))
                .when(categoryGateway).deleteById(eq(expectedID));

        Assertions.assertThrows(IllegalStateException.class, () -> useCase.execute(expectedID.getValue()));

        Mockito.verify(categoryGateway, times(1)).deleteById(eq(expectedID));
    }

    private void save(final Category... aCategory) {
        List<CategoryJPAEntity> entities = Arrays.stream(aCategory).map(CategoryJPAEntity::from).toList();
        categoryRepository.saveAllAndFlush(entities);
    }

}
