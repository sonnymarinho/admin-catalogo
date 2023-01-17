package com.admin.catalogo.application.category.category.delete;

import com.admin.catalogo.domain.category.Category;
import com.admin.catalogo.domain.category.CategoryGateway;
import com.admin.catalogo.domain.category.CategoryID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeleteCategoryUseCaseTest {

    @InjectMocks
    private DefaultDeleteCategoryUseCase useCase;

    @Mock
    private CategoryGateway categoryGateway;

    @BeforeEach
    void cleanUp() {
        reset(categoryGateway);
    }

    @Test
    void givenAValidId_whenCallsDeleteCategory_shouldBeOK() {
        final var aCategory = Category.newCategory("Filmes", "A categoria mais assistida");

        CategoryID expectedID = aCategory.getId();

        doNothing().when(categoryGateway).deleteById(eq(expectedID));

        Assertions.assertDoesNotThrow(() -> useCase.execute(expectedID.getValue()));

        Mockito.verify(categoryGateway, times(1)).deleteById(eq(expectedID));
    }

    @Test
    void givenAnInvalidId_whenCallsDeleteCategory_shouldBeOK() {
        CategoryID expectedID = CategoryID.from("123");

        doNothing().when(categoryGateway).deleteById(eq(expectedID));

        Assertions.assertDoesNotThrow(() -> useCase.execute(expectedID.getValue()));

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
}