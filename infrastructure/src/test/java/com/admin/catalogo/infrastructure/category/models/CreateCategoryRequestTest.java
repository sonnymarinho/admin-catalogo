package com.admin.catalogo.infrastructure.category.models;

import com.admin.catalogo.JacksonTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.json.JacksonTester;

@JacksonTest
public class CreateCategoryRequestTest {
    @Autowired
    private JacksonTester<CreateCategoryRequest> json;


    @Test
    public void testUnmarshall() throws Exception {
        final var expectedName = "Filmes";
        final var expectedDescription = "Filmes de ação";
        final var expectedIsActive = true;

        final var json =
                """
                {
                    "name": "%s",
                    "description": "%s",
                    "is_active": %s
                }
                """.formatted(
                        expectedName,
                        expectedDescription,
                        expectedIsActive
                );

        final var actualJson = this.json.parse(json);

        Assertions.assertThat(actualJson)
                .hasFieldOrPropertyWithValue("name", expectedName)
                .hasFieldOrPropertyWithValue("description", expectedDescription)
                .hasFieldOrPropertyWithValue("active", expectedIsActive);
    }

    @Test
    public void testMarshall() throws Exception {
        final var expectedName = "Filmes";
        final var expectedDescription = "Filmes de ação";
        final var expectedIsActive = true;

        final var request = new CreateCategoryRequest(
                expectedName,
                expectedDescription,
                expectedIsActive
        );

        final var actualJson = this.json.write(request);

        Assertions.assertThat(actualJson)
                .hasJsonPathStringValue("$.name", expectedName)
                .hasJsonPathStringValue("$.description", expectedDescription)
                .hasJsonPathBooleanValue("$.is_active", expectedIsActive);
    }
}
