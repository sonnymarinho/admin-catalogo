package com.admin.catalogo.application.category.retrieve.get;

import com.admin.catalogo.IntegrationTest;
import com.admin.catalogo.application.category.category.retrieve.get.CategoryOutput;
import com.admin.catalogo.application.category.category.retrieve.get.GetCategoryByIdUseCase;
import com.admin.catalogo.domain.category.Category;
import com.admin.catalogo.domain.category.CategoryGateway;
import com.admin.catalogo.domain.category.CategoryID;
import com.admin.catalogo.domain.exceptions.DomainException;
import com.admin.catalogo.infrastructure.category.CategoryRepository;
import com.admin.catalogo.infrastructure.category.persistence.CategoryJPAEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@IntegrationTest
public class GetCategoryByIdUseCaseIT {

    @Autowired
    private GetCategoryByIdUseCase useCase;

    @Autowired
    private CategoryRepository categoryRepository;

    @SpyBean
    private CategoryGateway categoryGateway;

    @Test
    void givenAValidId_whenCallsDeleteCategory_shouldBeOK() {
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedState = true;

        Category aCategory = Category.newCategory(expectedName, expectedDescription);
        var expectedId = aCategory.getId();

        save(aCategory);

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

    private void save(final Category... aCategory) {
        List<CategoryJPAEntity> entities = Arrays.stream(aCategory).map(CategoryJPAEntity::from).toList();
        categoryRepository.saveAllAndFlush(entities);
    }

    @Test
    void givenAValidId_whenCallsDeleteCategory_shouldReturnNotFound() {
        final var expectedId = CategoryID.from("123");

        final var expectedErrorMessage = "Category with ID %s was not found".formatted(expectedId.getValue());

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

        Mockito.doThrow(new IllegalStateException(expectedErrorMessage))
                .when(categoryGateway).findById(Mockito.eq(expectedId));

        final var actualException = Assertions.assertThrows(
                IllegalStateException.class,
                () -> useCase.execute(expectedId.getValue())
        );

        final var actualErrorMessage = actualException.getMessage();
        Assertions.assertEquals(actualErrorMessage, expectedErrorMessage);
    }

}
