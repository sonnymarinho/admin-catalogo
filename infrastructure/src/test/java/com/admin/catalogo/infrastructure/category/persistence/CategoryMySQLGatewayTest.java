package com.admin.catalogo.infrastructure.category.persistence;

import com.admin.catalogo.domain.category.Category;
import com.admin.catalogo.infrastructure.category.CategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;


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
}