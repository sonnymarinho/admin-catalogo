package com.admin.catalogo.infrastructure.category.models;

import com.admin.catalogo.JacksonTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.json.JacksonTester;

import java.time.Instant;

@JacksonTest
public class CategoryListResponseTest {
    @Autowired
    private JacksonTester<CategoryListResponse> json;

    @Test
    public void testMarshall() throws Exception {
        final var expectedId = "123";
        final var expectedName = "Filmes";
        final var expectedDescription = "Filmes de ação";
        final var expectedCreatedAt = Instant.now();
        final var expectedUpdatedAt = Instant.now();
        final var expectedDeletedAt = Instant.now();
        final var expectedIsActive = false;

        final var response = new CategoryListResponse(
                expectedId,
                expectedName,
                expectedDescription,
                expectedIsActive,
                expectedCreatedAt,
                expectedUpdatedAt,
                expectedDeletedAt
        );

        final var actualJson = this.json.write(response);

        Assertions.assertThat(actualJson)
                .hasJsonPathStringValue("$.id", expectedId)
                .hasJsonPathStringValue("$.name", expectedName)
                .hasJsonPathStringValue("$.description", expectedDescription)
                .hasJsonPathBooleanValue("$.is_active", expectedIsActive)
                .hasJsonPathStringValue("$.created_at", expectedCreatedAt.toString())
                .hasJsonPathStringValue("$.updated_at", expectedUpdatedAt.toString())
                .hasJsonPathStringValue("$.deleted_at", expectedDeletedAt.toString());
    }
}
