package com.admin.catalogo.application.category.retrieve;

import com.admin.catalogo.application.category.retrieve.get.CategoryOutput;
import com.admin.catalogo.domain.category.Category;
import com.admin.catalogo.domain.category.CategoryGateway;
import com.admin.catalogo.domain.category.CategoryID;
import com.admin.catalogo.domain.exceptions.DomainException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class GetCategoryByIdUseCaseTest {


    @InjectMocks
    private DefaultGetCategoryByIdUseCase useCase;

    @Mock
    CategoryGateway categoryGateway;


    @BeforeEach
    void cleanUp() {
        Mockito.reset(categoryGateway);
    }

    @Test
    void givenAValidId_whenCallsDeleteCategory_shouldBeOK() {
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedState = true;

        Category aCategory = Category.newCategory(expectedName, expectedDescription);
        var expectedId = aCategory.getId();

        Mockito.when(categoryGateway.findById(Mockito.eq(expectedId))).thenReturn(Optional.of(aCategory.clone()));

        final var actualCategory = useCase.execute(expectedId.getValue());

        Assertions.assertEquals(actualCategory.id(), expectedId);
        Assertions.assertEquals(actualCategory.name(), expectedName);
        Assertions.assertEquals(actualCategory.description(), expectedDescription);
        Assertions.assertEquals(actualCategory.isActive(), expectedState);
        Assertions.assertEquals(actualCategory.createdAt(), aCategory.getCreatedAt());
        Assertions.assertEquals(actualCategory.updatedAt(), aCategory.getUpdatedAt());
        Assertions.assertEquals(actualCategory.deletedAt(), aCategory.getDeletedAt());
        Assertions.assertEquals(CategoryOutput.from(aCategory), actualCategory);
    }

    @Test
    void givenAValidId_whenCallsDeleteCategory_shouldReturnNotFound() {
        final var expectedId = CategoryID.from("123");

        final var expectedErrorMessage = "Category with ID %s was not found".formatted(expectedId.getValue());

        Mockito.when(categoryGateway.findById(Mockito.eq(expectedId))).thenReturn(Optional.empty());

        final var actualException = Assertions.assertThrows(
                DomainException.class,
                () -> useCase.execute(expectedId.getValue())
        );

        final var actualErrorMessage = actualException.getMessage();

        Assertions.assertEquals(actualErrorMessage, expectedErrorMessage);
    }

    @Test
    void givenAValidId_whenTheGatewayThrowsException_shouldReturnException() {
        final var expectedId = CategoryID.from("123");
        final var expectedErrorMessage = "Supposed message";

        Mockito.when(categoryGateway.findById(Mockito.eq(expectedId)))
                .thenThrow(new IllegalStateException(expectedErrorMessage));

        final var actualException = Assertions.assertThrows(
                IllegalStateException.class,
                () -> useCase.execute(expectedId.getValue())
        );

        final var actualErrorMessage = actualException.getMessage();
        Assertions.assertEquals(actualErrorMessage, expectedErrorMessage);
    }
}