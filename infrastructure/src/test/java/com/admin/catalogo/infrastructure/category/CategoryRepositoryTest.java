package com.admin.catalogo.infrastructure.category;

import com.admin.catalogo.domain.category.Category;
import com.admin.catalogo.infrastructure.category.persistence.CategoryJPAEntity;
import com.admin.catalogo.infrastructure.category.persistence.MySQLGatewayTest;
import org.hibernate.PropertyValueException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

import static org.junit.jupiter.api.Assertions.*;

@MySQLGatewayTest
class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    public void givenAnInvalidNullName_whenCallsSave_shouldReturnError() {
        final var expectedPropertyName = "name";
        final var expectedPropertyCauseMessage = "not-null property references a null or transient value : com.admin.catalogo.infrastructure.category.persistence.CategoryJPAEntity.%s".formatted(expectedPropertyName);

        Category aCategory = Category.newCategory("Filmes", "Descrição da categoria");

        final var anEntity = CategoryJPAEntity.from(aCategory);
        anEntity.setName(null);

        final var actualException =
                assertThrows(DataIntegrityViolationException.class, () -> categoryRepository.save(anEntity));

        final var actualCause =
                assertInstanceOf(PropertyValueException.class, actualException.getCause());

        Assertions.assertEquals(expectedPropertyName, actualCause.getPropertyName());
        Assertions.assertEquals(expectedPropertyCauseMessage, actualCause.getMessage());
    }

    @Test
    public void givenAnInvalidNullCreatedAt_whenCallsSave_shouldReturnError() {
        final var expectedPropertyName = "createdAt";
        final var expectedPropertyCauseMessage = "not-null property references a null or transient value : com.admin.catalogo.infrastructure.category.persistence.CategoryJPAEntity.%s".formatted(expectedPropertyName);

        Category aCategory = Category.newCategory("Filmes", "Descrição da categoria");

        final var anEntity = CategoryJPAEntity.from(aCategory);
        anEntity.setCreatedAt(null);

        final var actualException =
                assertThrows(DataIntegrityViolationException.class, () -> categoryRepository.save(anEntity));

        final var actualCause =
                assertInstanceOf(PropertyValueException.class, actualException.getCause());

        Assertions.assertEquals(expectedPropertyName, actualCause.getPropertyName());
        Assertions.assertEquals(expectedPropertyCauseMessage, actualCause.getMessage());
    }

    @Test
    public void givenAnInvalidNullUpdatedAt_whenCallsSave_shouldReturnError() {
        final var expectedPropertyName = "updatedAt";
        final var expectedPropertyCauseMessage = "not-null property references a null or transient value : com.admin.catalogo.infrastructure.category.persistence.CategoryJPAEntity.%s".formatted(expectedPropertyName);

        Category aCategory = Category.newCategory("Filmes", "Descrição da categoria");

        final var anEntity = CategoryJPAEntity.from(aCategory);
        anEntity.setUpdatedAt(null);

        final var actualException =
                assertThrows(DataIntegrityViolationException.class, () -> categoryRepository.save(anEntity));

        final var actualCause =
                assertInstanceOf(PropertyValueException.class, actualException.getCause());

        Assertions.assertEquals(expectedPropertyName, actualCause.getPropertyName());
        Assertions.assertEquals(expectedPropertyCauseMessage, actualCause.getMessage());
    }
}