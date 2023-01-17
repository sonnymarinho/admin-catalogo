package com.admin.catalogo.application.category.create;

import com.admin.catalogo.IntegrationTest;
import com.admin.catalogo.application.category.category.create.CreateCategoryCommand;
import com.admin.catalogo.application.category.category.create.CreateCategoryOutput;
import com.admin.catalogo.application.category.category.create.CreateCategoryUseCase;
import com.admin.catalogo.application.category.category.create.DefaultCreateCategoryUseCase;
import com.admin.catalogo.application.category.category.retrieve.get.CategoryOutput;
import com.admin.catalogo.domain.category.CategoryGateway;
import com.admin.catalogo.infrastructure.category.CategoryRepository;
import com.admin.catalogo.infrastructure.category.persistence.CategoryJPAEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.AdditionalAnswers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@IntegrationTest
public class CreateCategoryUseCaseIT {

    @Autowired
    private DefaultCreateCategoryUseCase useCase;

    @Autowired
    private CategoryRepository categoryRepository;

    @SpyBean
    CategoryGateway categoryGateway;

    @Test
    public void givenAValidComand_whenCallsCreateCategory_shouldReturnCategoryId() {
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;

        Assertions.assertEquals(0, categoryRepository.count());

        final var aComand =
                CreateCategoryCommand.with(expectedName, expectedDescription, expectedIsActive);

        final var actualOutput = useCase.execute(aComand).get();

        assertNotNull(actualOutput);
        assertNotNull(actualOutput.id());

        Assertions.assertEquals(1, categoryRepository.count());

        final var actualCategory = categoryRepository.findById(actualOutput.id().getValue()).get();

        Assertions.assertEquals(actualCategory.getName(), expectedName);
        Assertions.assertEquals(actualCategory.getDescription(), expectedDescription);
        Assertions.assertEquals(actualCategory.isActive(), expectedIsActive);
        Assertions.assertNotNull(actualCategory.getCreatedAt());
        Assertions.assertNotNull(actualCategory.getUpdatedAt());
        Assertions.assertNull(actualCategory.getDeletedAt());
    }

    @Test
    public void givenAInvalidName_whenCallsCreateCategory_thenShouldReturnDomainException() {
        final String expectedName = null;
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;
        final var expectedErrorMessage = "'name' should not be null";
        final var expectedErrorCount = 1;

        Assertions.assertEquals(0, categoryRepository.count());

        final var aComand =
                CreateCategoryCommand.with(expectedName, expectedDescription, expectedIsActive);

//        final var actualException = assertThrows(DomainException.class, () -> useCase.execute(aComand));
        final var notification = useCase.execute(aComand).getLeft();

        assertEquals(expectedErrorCount, notification.getErrors().size());
        assertEquals(expectedErrorMessage, notification.firstError().message());

        Assertions.assertEquals(0, categoryRepository.count());

        verify(categoryGateway, times(0)).create(any());
    }

    @Test
    public void givenAValidComandWithInactiveCategory_whenCallsCreateCategory_shouldReturnInactiveCategoryId() {
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = false;

        Assertions.assertEquals(0, categoryRepository.count());

        final var aComand = CreateCategoryCommand.with(expectedName, expectedDescription, expectedIsActive);

        final CreateCategoryOutput actualOutput = useCase.execute(aComand).get();

        assertNotNull(actualOutput);
        assertNotNull(actualOutput.id());

        Assertions.assertEquals(1, categoryRepository.count());

        final var actualCategory = categoryRepository.findById(actualOutput.id().getValue()).get();

        Assertions.assertEquals(actualCategory.getName(), expectedName);
        Assertions.assertEquals(actualCategory.getDescription(), expectedDescription);
        Assertions.assertEquals(actualCategory.isActive(), expectedIsActive);
        Assertions.assertNotNull(actualCategory.getCreatedAt());
        Assertions.assertNotNull(actualCategory.getUpdatedAt());
        Assertions.assertNotNull(actualCategory.getDeletedAt());
    }


    @Test
    public void givenAValidComand_whenGatewayTrhowsRandomException_shouldReturnAException() {
        final String expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;
        final var expectedErrorMessage = "Generic Gateway Error";
        final var expectedErrorCount = 1;

        final var aComand =
                CreateCategoryCommand.with(expectedName, expectedDescription, expectedIsActive);

//        USADO QUANDO É UTILIZADO MOCK
//        when(categoryGateway.create(any())).thenThrow(new IllegalStateException(expectedErrorMessage));

//        USADO QUANDO É UTILIZADO SPY BEAN (COMPONENTE EXISTE)
        doThrow(new IllegalStateException(expectedErrorMessage))
                .when(categoryGateway).create(any());

        final var notification = useCase.execute(aComand).getLeft();

        assertEquals(expectedErrorCount, notification.getErrors().size());
        assertEquals(expectedErrorMessage, notification.firstError().message());
    }
}
