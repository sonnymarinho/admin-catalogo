package com.admin.catalogo.domain.category;

import com.admin.catalogo.domain.category.Category;
import com.admin.catalogo.domain.exceptions.DomainException;
import com.admin.catalogo.domain.validation.handler.ThrowsValidationHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class CategoryTest {
    @Test
    void givenAValidParams_whenCallNewCategory_thenInstantiateACategory() {
        final var expectedName = "Filmes";
        final var expectedDescription = "A Categoria Mais Assistida";
        final var expectedIsActive = true;

        final var actualCategory = Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        Assertions.assertNotNull(actualCategory);
        Assertions.assertNotNull(actualCategory.getId());
        Assertions.assertEquals(expectedName, actualCategory.getName());
        Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
        Assertions.assertEquals(expectedIsActive, actualCategory.isActive());

        Assertions.assertNotNull(actualCategory.getCreatedAt());
        Assertions.assertNotNull(actualCategory.getUpdatedAt());
        Assertions.assertNull(actualCategory.getDeletedAt());
    }

    @Test
    void givenAnInvalidNullName_whenCallNewCategoryAndValidate_thenShouldReceiveError() {
        final String expectedName = null;
        final var expectedDescription = "A Categoria Mais Assistida";
        final var expectedIsActive = true;

        final var actualCategory = Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        final var actualException = Assertions.assertThrows(DomainException.class, () ->  actualCategory.validate(new ThrowsValidationHandler()));

        String expectedErrorMessage = "'name' should not be null";
        final var expectedErrorCount = 1;
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }

    @Test
    void givenABlankName_whenCallNewCategoryAndValidate_thenShouldReceiveError() {
        final String expectedName = "   ";
        final var expectedDescription = "A Categoria Mais Assistida";
        final var expectedIsActive = true;

        final var actualCategory = Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        final var actualException = Assertions.assertThrows(DomainException.class, () ->  actualCategory.validate(new ThrowsValidationHandler()));

        String expectedErrorMessage = "'name' should not be blank";
        final var expectedErrorCount = 1;
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }

    @Test
    void givenAnInvalidNameLengthMoreThan255_whenCallNewCategoryAndValidate_thenShouldReceiveError() {
        final String expectedName = "Fi ";
        final var expectedDescription = """
                Mussum Ipsum, cacilds vidis litro abertis. Mé faiz elementum girarzis,
                nisi eros vermeio.Leite de capivaris, leite de mula manquis sem cabeça.
                Nullam volutpat risus nec leo commodo, ut interdum diam laoreet. Sed
                non consequat odio.Posuere libero varius. Nullam a nisl ut ante blandit
                hendrerit. Aenean sit amet nisi.
                                
                Per aumento de cachacis, eu reclamis.Não sou faixa preta cumpadi, sou
                preto inteiris, inteiris.Si u mundo tá muito paradis? Toma um mé que o 
                mundo vai girarzis!Posuere libero varius. Nullam a nisl ut ante blandit 
                hendrerit. Aenean sit amet nisi.
                """;
        final var expectedIsActive = true;
        String expectedErrorMessage = "'name' must be between 3 and 255 characters";
        final var expectedErrorCount = 1;

        final var actualCategory = Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        final var actualException = Assertions.assertThrows(DomainException.class, () ->  actualCategory.validate(new ThrowsValidationHandler()));

        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }

    @Test
    void givenAnNullDescriptionLengthLessThan3_whenCallNewCategoryAndValidate_thenShouldReceiveError() {
        final String expectedName = "Filmes";
        final String expectedDescription = null;
        final var expectedIsActive = true;

        final var actualCategory = Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        final var actualException = Assertions.assertThrows(DomainException.class, () ->  actualCategory.validate(new ThrowsValidationHandler()));

        String expectedErrorMessage = "'description' should not be null";
        final var expectedErrorCount = 1;
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }

    @Test
    void givenAnInvalidDescriptionLengthLessThan3_whenCallNewCategoryAndValidate_thenShouldReceiveError() {
        final String expectedName = "Filmes";
        final var expectedDescription = "A ";
        final var expectedIsActive = true;

        final var actualCategory = Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        final var actualException = Assertions.assertThrows(DomainException.class, () ->  actualCategory.validate(new ThrowsValidationHandler()));

        String expectedErrorMessage = "'description' must be between 3 and 255 characters";
        final var expectedErrorCount = 1;
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }

    @Test
    void givenAnInvalidDescriptionLengthMoreThan255_whenCallNewCategoryAndValidate_thenShouldReceiveError() {
        final String expectedName = "Filmes";
        final var expectedIsActive = true;
        final var expectedDescription = """
                Mussum Ipsum, cacilds vidis litro abertis. Mé faiz elementum girarzis,
                nisi eros vermeio.Leite de capivaris, leite de mula manquis sem cabeça.
                Nullam volutpat risus nec leo commodo, ut interdum diam laoreet. Sed
                non consequat odio.Posuere libero varius. Nullam a nisl ut ante blandit
                hendrerit. Aenean sit amet nisi.
                                
                Per aumento de cachacis, eu reclamis.Não sou faixa preta cumpadi, sou
                preto inteiris, inteiris.Si u mundo tá muito paradis? Toma um mé que o 
                mundo vai girarzis!Posuere libero varius. Nullam a nisl ut ante blandit 
                hendrerit. Aenean sit amet nisi.
                """;
        String expectedErrorMessage = "'description' must be between 3 and 255 characters";
        final var expectedErrorCount = 1;

        final var actualCategory = Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        final var actualException = Assertions.assertThrows(DomainException.class, () ->  actualCategory.validate(new ThrowsValidationHandler()));

        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }

    @Test
    void givenAnValidEmptyDescription_whenCallNewCategoryAndValidate_thenShouldReceiveError() {
        final String expectedName = "Filmes";
        final var expectedDescription = "A C";
        final var expectedIsActive = true;

        final var actualCategory = Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        Assertions.assertDoesNotThrow(() ->  actualCategory.validate(new ThrowsValidationHandler()));

        Assertions.assertNotNull(actualCategory);
        Assertions.assertNotNull(actualCategory.getId());
        Assertions.assertEquals(expectedName, actualCategory.getName());
        Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
        Assertions.assertEquals(expectedIsActive, actualCategory.isActive());
        Assertions.assertNotNull(actualCategory.getCreatedAt());
        Assertions.assertNotNull(actualCategory.getUpdatedAt());
        Assertions.assertNull(actualCategory.getDeletedAt());
    }
}