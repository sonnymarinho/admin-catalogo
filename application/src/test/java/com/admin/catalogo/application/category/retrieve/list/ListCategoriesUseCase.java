package com.admin.catalogo.application.category.retrieve.list;

import com.admin.catalogo.application.category.retrieve.get.CategoryOutput;
import com.admin.catalogo.domain.category.Category;
import com.admin.catalogo.domain.category.CategoryGateway;
import com.admin.catalogo.domain.category.CategorySearchQuery;
import com.admin.catalogo.domain.pagination.Pagination;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

@ExtendWith(MockitoExtension.class)
public class ListCategoriesUseCase {

    @InjectMocks
    DefaultListCategoriesUseCase useCase;

    @Mock
    private CategoryGateway categoryGateway;

    @BeforeEach
    void cleanUp() {
        Mockito.reset(categoryGateway);
    }

    @Test
    public void givenAValidQuery_whenCallsListCategories_thenShouldReturnCategories() {
        final var categories = List.of(
                Category.newCategory("Filmes", null),
                Category.newCategory("Series", null)
        );

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "";
        final var expectedSort = "createdAt";
        final var expectedExpectedDirection = "asc";

        final var aQuery = new CategorySearchQuery(
                expectedPage,
                expectedPerPage,
                expectedTerms,
                expectedSort,
                expectedExpectedDirection
        );

        final var expectedPagination = new Pagination<Category>(
                expectedPage,
                expectedPerPage,
                categories.size(),
                categories
        );

        final var expectedItensCount = 2;
        final var expectedResult = expectedPagination.map(CategoryListOutput::from);


        Mockito.when(categoryGateway.findAll(Mockito.eq(aQuery)))
                .thenReturn(expectedPagination);


        final var actualResult = useCase.execute(aQuery);

        Assertions.assertEquals(expectedItensCount, actualResult.items().size());
        Assertions.assertEquals(expectedResult, actualResult);
        Assertions.assertEquals(expectedPage, actualResult.currentPage());
        Assertions.assertEquals(expectedPerPage, actualResult.perPage());
        Assertions.assertEquals(categories.size(), actualResult.total());
    }

    @Test
    public void givenAValidQuery_whenHasNoResults_thenShouldReturnEmptyCategories() {
        final var categories = List.<Category>of();

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "";
        final var expectedSort = "createdAt";
        final var expectedExpectedDirection = "asc";

        final var aQuery = new CategorySearchQuery(
                expectedPage,
                expectedPerPage,
                expectedTerms,
                expectedSort,
                expectedExpectedDirection
        );

        final var expectedPagination = new Pagination<Category>(
                expectedPage,
                expectedPerPage,
                categories.size(),
                categories
        );

        final var expectedItensCount = 0;
        final var expectedResult = expectedPagination.map(CategoryListOutput::from);


        Mockito.when(categoryGateway.findAll(Mockito.eq(aQuery)))
                .thenReturn(expectedPagination);


        final var actualResult = useCase.execute(aQuery);

        Assertions.assertEquals(expectedItensCount, actualResult.items().size());
        Assertions.assertEquals(expectedResult, actualResult);
        Assertions.assertEquals(expectedPage, actualResult.currentPage());
        Assertions.assertEquals(expectedPerPage, actualResult.perPage());
        Assertions.assertEquals(categories.size(), actualResult.total());
    }

    @Test
    public void givenAValidQuery_whenGatewayThrowsException_thenShouldReturnException() {
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "";
        final var expectedSort = "createdAt";
        final var expectedExpectedDirection = "asc";
        final var expectedErrorMessage = "Supposed gateway error message";

        final var aQuery = new CategorySearchQuery(
                expectedPage,
                expectedPerPage,
                expectedTerms,
                expectedSort,
                expectedExpectedDirection
        );

        Mockito.when(categoryGateway.findAll(Mockito.eq(aQuery)))
                .thenThrow(new IllegalStateException(expectedErrorMessage));

        final var actualException = Assertions.assertThrows(
                IllegalStateException.class,
                () -> useCase.execute(aQuery)
        );

        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());
    }
}
