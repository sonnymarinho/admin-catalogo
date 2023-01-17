package com.admin.catalogo.application.category.retrieve.list;

import com.admin.catalogo.IntegrationTest;
import com.admin.catalogo.application.category.category.retrieve.list.CategoryListOutput;
import com.admin.catalogo.application.category.category.retrieve.list.ListCategoriesUseCase;
import com.admin.catalogo.domain.category.Category;
import com.admin.catalogo.domain.category.CategorySearchQuery;
import com.admin.catalogo.domain.pagination.Pagination;
import com.admin.catalogo.infrastructure.category.CategoryRepository;
import com.admin.catalogo.infrastructure.category.persistence.CategoryJPAEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;

@IntegrationTest
public class ListCategoriesUseCaseIT {

    @Autowired
    ListCategoriesUseCase listCategoriesUseCase;

    @Autowired
    CategoryRepository categoryRepository;

    @BeforeEach
    public void mockUp() {
        final var categories = List.of(
                Category.newCategory("Filmes", null)
                , Category.newCategory("Netflix Originals", "Títulos de autoria da Netflix")
                , Category.newCategory("Amazon Originals", "Títulos de autoria da Amazon Prime")
                , Category.newCategory("Documentários", null)
                , Category.newCategory("Sports", null)
                , Category.newCategory("Kids", "Conteúdo destinado a crianças")
                , Category.newCategory("Series", null)
        );

        categoryRepository.saveAllAndFlush(categories.stream().map(CategoryJPAEntity::from).toList());
    }


    @Test
    public void givenAValidTerm_whenTermDoesNotMatchesPersistent_shouldReturnEmpty() {
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "<invalid term>";
        final var expectedSort = "name";
        final var expectedDirection = "asc";
        final var expectedItemsCount = 0;
        final var expectedTotal = 0;

        final var aQuery = new CategorySearchQuery(
                expectedPage,
                expectedPerPage,
                expectedTerms,
                expectedSort,
                expectedDirection
        );

        final var actualResult = listCategoriesUseCase.execute(aQuery);

        Assertions.assertEquals(expectedItemsCount, actualResult.items().size());
        Assertions.assertEquals(expectedPage, actualResult.currentPage());
        Assertions.assertEquals(expectedPerPage, actualResult.perPage());
        Assertions.assertEquals(expectedTotal, actualResult.total());
    }

    @ParameterizedTest
    @CsvSource({
            "fil,0,10,1,1,Filmes"
            ,"net,0,10,1,1,Netflix Originals"
            ,"ZON,0,10,1,1,Amazon Originals"
            ,"da amazon,0,10,1,1,Amazon Originals"
            ,"KI,0,10,1,1,Kids"
            ,"crianças,0,10,1,1,Kids"
    })
    void givenAValidTerm_whenCallsListCategories_thenShouldReturnCategoriesFiltered(
            final String expectedTerms
            , final int expectedPage
            , final int expectedPerPage
            , final int expectedItemsCount
            , final long expectedTotal
            , final String expectedCategoryName
    ) {
        final var expectedSort = "name";
        final var expectedDirection = "asc";

        final var aQuery = new CategorySearchQuery(
                expectedPage,
                expectedPerPage,
                expectedTerms,
                expectedSort,
                expectedDirection
        );

        final var actualResult = listCategoriesUseCase.execute(aQuery);

        Assertions.assertEquals(expectedItemsCount, actualResult.items().size());
        Assertions.assertEquals(expectedPage, actualResult.currentPage());
        Assertions.assertEquals(expectedPerPage, actualResult.perPage());
        Assertions.assertEquals(expectedTotal, actualResult.total());
        Assertions.assertEquals(expectedCategoryName, actualResult.items().get(0).name());
    }

    @ParameterizedTest
    @CsvSource({
            "name,asc,0,10,7,7,Amazon Originals"
            ,"name,desc,0,10,7,7,Sports"
            ,"createdAt,asc,0,10,7,7,Filmes"
            ,"createdAt,desc,0,10,7,7,Series"
    })
    void givenAValidSortAndDirection_whenCallsListCategories_thenShouldReturnCategoriesOrdered(
            final String expectedSort
            , final String expectedDirection
            , final int expectedPage
            , final int expectedPerPage
            , final int expectedItemsCount
            , final long expectedTotal
            , final String expectedCategoryName
    ) {
        final var expectedTerms = "";

        final var aQuery = new CategorySearchQuery(
                expectedPage,
                expectedPerPage,
                expectedTerms,
                expectedSort,
                expectedDirection
        );

        final var actualResult = listCategoriesUseCase.execute(aQuery);

        Assertions.assertEquals(expectedItemsCount, actualResult.items().size());
        Assertions.assertEquals(expectedPage, actualResult.currentPage());
        Assertions.assertEquals(expectedPerPage, actualResult.perPage());
        Assertions.assertEquals(expectedTotal, actualResult.total());
        Assertions.assertEquals(expectedCategoryName, actualResult.items().get(0).name());
    }

    @ParameterizedTest
    @CsvSource({
              "0,2,2,7,Amazon Originals;Documentários"
            , "1,2,2,7,Filmes;Kids"
            , "2,2,2,7,Netflix Originals;Series"
            , "3,2,1,7,Sports"
    })
    public void givenAValidPage_whenCallsListCategories_thenShouldReturnCategoriesPaginated(
              final int expectedPage
            , final int expectedPerPage
            , final int expectedItemsCount
            , final long expectedTotal
            , final String expectedCategoriesName
    ) {
        final var expectedTerms = "";
        final var expectedSort = "name";
        final var expectedDirection = "asc";

        final var aQuery = new CategorySearchQuery(
                expectedPage,
                expectedPerPage,
                expectedTerms,
                expectedSort,
                expectedDirection
        );

        final var actualResult = listCategoriesUseCase.execute(aQuery);

        int index = 0;
        for (String expectedName : expectedCategoriesName.split(";")) {
            final var actualName = actualResult.items().get(index).name();

            Assertions.assertEquals(expectedName, actualName);
            index++;
        }
    }
}
