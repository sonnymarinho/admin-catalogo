package com.admin.catalogo.infrastructure.category.persistence;

import com.admin.catalogo.MySQLGatewayTest;
import com.admin.catalogo.domain.category.Category;
import com.admin.catalogo.domain.category.CategoryID;
import com.admin.catalogo.domain.category.CategorySearchQuery;
import com.admin.catalogo.domain.pagination.Pagination;
import com.admin.catalogo.infrastructure.category.CategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;


@MySQLGatewayTest
class CategoryMySQLGatewayTest {

    @Autowired
    private CategoryMySQLGateway categoryMySQLGateway;

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    public void testInjectDependencies() {
        Assertions.assertNotNull(categoryMySQLGateway);
        Assertions.assertNotNull(categoryRepository);
    }

    @Test
    void givenAValidCategory_whenCallsCreate_shouldReturnANewCategory() {
        final var expectedName = "Filmes";
        final var expectedDescription = "Descrição da categoria";
        final var expectedIsActive = true;

        final var aCategory = Category.newCategory(expectedName, expectedDescription);

        Assertions.assertEquals(0, categoryRepository.count());

        final var actualCategory = categoryMySQLGateway.create(aCategory);

        Assertions.assertEquals(1, categoryRepository.count());

        Assertions.assertEquals(actualCategory.getId(), aCategory.getId());
        Assertions.assertEquals(actualCategory.getName(), expectedName);
        Assertions.assertEquals(actualCategory.getDescription(), expectedDescription);
        Assertions.assertEquals(actualCategory.isActive(), expectedIsActive);
        Assertions.assertEquals(actualCategory.getCreatedAt(), aCategory.getCreatedAt());
        Assertions.assertEquals(actualCategory.getUpdatedAt(), aCategory.getUpdatedAt());
        Assertions.assertEquals(actualCategory.getDeletedAt(), aCategory.getDeletedAt());
        Assertions.assertNull(actualCategory.getDeletedAt());

        final var actualEntity = categoryRepository.findById(aCategory.getId().getValue()).get();

        Assertions.assertEquals(actualEntity.getId(), aCategory.getId().getValue());
        Assertions.assertEquals(actualEntity.getName(), expectedName);
        Assertions.assertEquals(actualEntity.getDescription(), expectedDescription);
        Assertions.assertEquals(actualEntity.isActive(), expectedIsActive);
        Assertions.assertEquals(actualEntity.getCreatedAt(), aCategory.getCreatedAt());
        Assertions.assertEquals(actualEntity.getUpdatedAt(), aCategory.getUpdatedAt());
        Assertions.assertEquals(actualEntity.getDeletedAt(), aCategory.getDeletedAt());
        Assertions.assertNull(actualEntity.getDeletedAt());
    }

    @Test
    void givenAValidCategory_whenCallsUpdate_shouldReturnACategory() {
        final var expectedName = "Filmes";
        final var expectedDescription = "Descrição da categoria";
        final var expectedIsActive = true;

        final var aCategory = Category.newCategory("Film", null);

        Assertions.assertEquals(0, categoryRepository.count());

        categoryRepository.saveAndFlush(CategoryJPAEntity.from(aCategory));
        Assertions.assertEquals(1, categoryRepository.count());

        final var actualInvalidEntity = categoryRepository.findById(aCategory.getId().getValue()).get();
        Assertions.assertEquals("Film", actualInvalidEntity.getName());
        Assertions.assertNull(actualInvalidEntity.getDescription());
        Assertions.assertEquals(expectedIsActive, actualInvalidEntity.isActive());

        final var aUpdatedCategory =  aCategory.clone().update(
                expectedName,
                expectedDescription,
                expectedIsActive
        );

        final var actualCategory = categoryMySQLGateway.update(aUpdatedCategory);

        Assertions.assertEquals(1, categoryRepository.count());

        Assertions.assertEquals(actualCategory.getId(), aCategory.getId());
        Assertions.assertEquals(actualCategory.getName(), expectedName);
        Assertions.assertEquals(actualCategory.getDescription(), expectedDescription);
        Assertions.assertEquals(actualCategory.isActive(), expectedIsActive);
        Assertions.assertEquals(actualCategory.getCreatedAt(), aCategory.getCreatedAt());
        Assertions.assertTrue(actualCategory.getUpdatedAt().isAfter(aCategory.getUpdatedAt()));
        Assertions.assertEquals(actualCategory.getDeletedAt(), aCategory.getDeletedAt());
        Assertions.assertNull(actualCategory.getDeletedAt());

        final var actualEntity = categoryRepository.findById(aCategory.getId().getValue()).get();

        Assertions.assertEquals(actualEntity.getId(), aUpdatedCategory.getId().getValue());
        Assertions.assertEquals(actualEntity.getName(), expectedName);
        Assertions.assertEquals(actualEntity.getDescription(), expectedDescription);
        Assertions.assertEquals(actualEntity.isActive(), expectedIsActive);
        Assertions.assertEquals(actualEntity.getCreatedAt(), aUpdatedCategory.getCreatedAt());
        Assertions.assertEquals(actualEntity.getUpdatedAt(), aUpdatedCategory.getUpdatedAt());
        Assertions.assertEquals(actualEntity.getDeletedAt(), aUpdatedCategory.getDeletedAt());
        Assertions.assertNull(actualEntity.getDeletedAt());
    }

    @Test
    public void givenAPrePersistedCategoryAndValidCategoryId_whenTryToDeleteIt_shouldDeleteCategory() {
        Category aCategory = Category.newCategory("Filmes", null);

        Assertions.assertEquals(0, categoryRepository.count());

        categoryRepository.saveAndFlush(CategoryJPAEntity.from(aCategory));

        Assertions.assertEquals(1, categoryRepository.count());

        categoryMySQLGateway.deleteById(aCategory.getId());

        Assertions.assertEquals(0, categoryRepository.count());
    }

    @Test
    public void givenInvalidCategoryId_whenTryToDeleteIt_shouldDeleteCategory() {
        Assertions.assertEquals(0, categoryRepository.count());

        categoryMySQLGateway.deleteById(CategoryID.from("<invalid>"));

        Assertions.assertEquals(0, categoryRepository.count());
    }

    @Test
    void givenAPrePersistedCategoryAndValidCategoryId_whenCallsFindById_shouldReturn() {
        final var expectedName = "Filmes";
        final var expectedDescription = "Descrição da categoria";
        final var expectedIsActive = true;

        final var aCategory = Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        Assertions.assertEquals(0, categoryRepository.count());

        categoryRepository.saveAndFlush(CategoryJPAEntity.from(aCategory));

        Assertions.assertEquals(1, categoryRepository.count());


        final var actualCategory = categoryMySQLGateway.findById(aCategory.getId()).get();

        Assertions.assertEquals(1, categoryRepository.count());

        Assertions.assertEquals(actualCategory.getId(), aCategory.getId());
        Assertions.assertEquals(actualCategory.getName(), expectedName);
        Assertions.assertEquals(actualCategory.getDescription(), expectedDescription);
        Assertions.assertEquals(actualCategory.isActive(), expectedIsActive);
        Assertions.assertEquals(actualCategory.getCreatedAt(), aCategory.getCreatedAt());
        Assertions.assertEquals(actualCategory.getUpdatedAt(), aCategory.getUpdatedAt());
        Assertions.assertEquals(actualCategory.getDeletedAt(), aCategory.getDeletedAt());
        Assertions.assertNull(actualCategory.getDeletedAt());
    }

    @Test
    void givenValidCategoryIdNotStored_whenCallsFindById_shouldReturnEmpty() {
        Assertions.assertEquals(0, categoryRepository.count());

        final var actualCategory = categoryMySQLGateway.findById(CategoryID.from(""));

        Assertions.assertTrue(actualCategory.isEmpty());
    }

    @Test
    public void givenPrePersistedCategories_whenCallsFindAll_shouldReturnPaginated() {
        final var expectedPage = 0;
        final var expectedPerPage = 1;
        final var expectedTotal = 3;

        final var filmes = Category.newCategory("Filmes", null);
        final var series = Category.newCategory("Series", null);
        final var documentarios = Category.newCategory("Documentarios", null);

        Assertions.assertEquals(0, categoryRepository.count());

        categoryRepository.saveAll(
                List.of(filmes, series, documentarios)
                        .stream().map(CategoryJPAEntity::from)
                        .toList()
        );

        Assertions.assertEquals(3, categoryRepository.count());

        final var  query = new CategorySearchQuery(0, 1, "", "name", "asc");
        Pagination<Category> actualResult = categoryMySQLGateway.findAll(query);

        Assertions.assertEquals(expectedPage, actualResult.currentPage());
        Assertions.assertEquals(expectedPerPage, actualResult.perPage());
        Assertions.assertEquals(expectedTotal, actualResult.total());
        Assertions.assertEquals(expectedPerPage, actualResult.items().size());

        Assertions.assertEquals(documentarios.getId(), actualResult.items().get(0).getId());
    }

    @Test
    public void givenEmptyCategoriesTable_whenCallsFindAll_shouldReturnEmptyPaginated() {
        final var expectedPage = 0;
        final var expectedPerPage = 1;
        final var expectedTotal = 0;

        Assertions.assertEquals(0, categoryRepository.count());

        final var  query = new CategorySearchQuery(0, 1, "", "name", "asc");
        Pagination<Category> actualResult = categoryMySQLGateway.findAll(query);

        Assertions.assertEquals(expectedPage, actualResult.currentPage());
        Assertions.assertEquals(expectedPerPage, actualResult.perPage());
        Assertions.assertEquals(expectedTotal, actualResult.total());
        Assertions.assertEquals(0, actualResult.items().size());
    }

    @Test
    public void givenFollowPagination_whenCallsFindAllWithPage1_shouldReturnPaginated() {
        var expectedPage = 0;
        final var expectedPerPage = 1;
        final var expectedTotal = 3;

        final var filmes = Category.newCategory("Filmes", null);
        final var series = Category.newCategory("Series", null);
        final var documentarios = Category.newCategory("Documentarios", null);

        Assertions.assertEquals(0, categoryRepository.count());

        categoryRepository.saveAll(
                List.of(filmes, series, documentarios)
                        .stream().map(CategoryJPAEntity::from)
                        .toList()
        );

        Assertions.assertEquals(3, categoryRepository.count());

        //Page 0
        var query = new CategorySearchQuery(0, 1, "", "name", "asc");
        var result = categoryMySQLGateway.findAll(query);

        Assertions.assertEquals(expectedPage, result.currentPage());
        Assertions.assertEquals(expectedPerPage, result.perPage());
        Assertions.assertEquals(expectedTotal, result.total());
        Assertions.assertEquals(expectedPerPage, result.items().size());

        Assertions.assertEquals(documentarios.getId(), result.items().get(0).getId());

        //Page 1
        expectedPage = 1;
        query = new CategorySearchQuery(1, 1, "", "name", "asc");
        result = categoryMySQLGateway.findAll(query);

        Assertions.assertEquals(expectedPage, result.currentPage());
        Assertions.assertEquals(expectedPerPage, result.perPage());
        Assertions.assertEquals(expectedTotal, result.total());
        Assertions.assertEquals(expectedPerPage, result.items().size());

        Assertions.assertEquals(filmes.getId(), result.items().get(0).getId());

        //Page 2
        expectedPage = 2;
         query = new CategorySearchQuery(2, 1, "", "name", "asc");
        result = categoryMySQLGateway.findAll(query);

        Assertions.assertEquals(expectedPage, result.currentPage());
        Assertions.assertEquals(expectedPerPage, result.perPage());
        Assertions.assertEquals(expectedTotal, result.total());
        Assertions.assertEquals(expectedPerPage, result.items().size());

        Assertions.assertEquals(series.getId(), result.items().get(0).getId());
    }

    @Test
    public void givenPrePersistedCategoriesAndDocAsTerms_whenCallsFindAllAndTermsMatchesCategoryName_shouldReturnPaginated() {
        final var expectedPage = 0;
        final var expectedPerPage = 1;
        final var expectedTotal = 1;

        final var filmes = Category.newCategory("Filmes", null);
        final var series = Category.newCategory("Series", null);
        final var documentarios = Category.newCategory("Documentarios", null);

        Assertions.assertEquals(0, categoryRepository.count());

        categoryRepository.saveAll(
                List.of(filmes, series, documentarios)
                        .stream().map(CategoryJPAEntity::from)
                        .toList()
        );

        Assertions.assertEquals(3, categoryRepository.count());

        final var  query = new CategorySearchQuery(0, 1, "doc", "name", "asc");
        Pagination<Category> actualResult = categoryMySQLGateway.findAll(query);

        Assertions.assertEquals(expectedPage, actualResult.currentPage());
        Assertions.assertEquals(expectedPerPage, actualResult.perPage());
        Assertions.assertEquals(expectedTotal, actualResult.total());
        Assertions.assertEquals(expectedPerPage, actualResult.items().size());

        Assertions.assertEquals(documentarios.getId(), actualResult.items().get(0).getId());
    }

    @Test
    public void givenPrePersistedCategoriesAndMaisAssistidaAsTerms_whenCallsFindAllAndTermsMatchesCategoryDescription_shouldReturnPaginated() {
        final var expectedPage = 0;
        final var expectedPerPage = 1;
        final var expectedTotal = 1;

        final var filmes = Category.newCategory("Filmes", "A categoria mais assistida");
        final var series = Category.newCategory("Series", "Uma categoria assistida");
        final var documentarios = Category.newCategory("Documentarios", "A categoria menos assistida");

        Assertions.assertEquals(0, categoryRepository.count());

        categoryRepository.saveAll(
                List.of(filmes, series, documentarios)
                        .stream().map(CategoryJPAEntity::from)
                        .toList()
        );

        Assertions.assertEquals(3, categoryRepository.count());

        final var  query = new CategorySearchQuery(0, 1, "MAIS ASSISTIDA", "name", "asc");
        Pagination<Category> actualResult = categoryMySQLGateway.findAll(query);

        Assertions.assertEquals(expectedPage, actualResult.currentPage());
        Assertions.assertEquals(expectedPerPage, actualResult.perPage());
        Assertions.assertEquals(expectedTotal, actualResult.total());
        Assertions.assertEquals(expectedPerPage, actualResult.items().size());

        Assertions.assertEquals(filmes.getId(), actualResult.items().get(0).getId());
    }

}