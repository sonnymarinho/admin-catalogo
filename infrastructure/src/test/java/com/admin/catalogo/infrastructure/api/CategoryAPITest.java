package com.admin.catalogo.infrastructure.api;

import com.admin.catalogo.ControllerTest;
import com.admin.catalogo.application.category.category.create.CreateCategoryOutput;
import com.admin.catalogo.application.category.category.create.CreateCategoryUseCase;
import com.admin.catalogo.application.category.category.delete.DeleteCategoryUseCase;
import com.admin.catalogo.application.category.category.retrieve.get.CategoryOutput;
import com.admin.catalogo.application.category.category.retrieve.get.GetCategoryByIdUseCase;
import com.admin.catalogo.application.category.category.update.UpdateCategoryOutput;
import com.admin.catalogo.application.category.category.update.UpdateCategoryUseCase;
import com.admin.catalogo.domain.category.Category;
import com.admin.catalogo.domain.category.CategoryID;
import com.admin.catalogo.domain.exceptions.DomainException;
import com.admin.catalogo.domain.exceptions.NotFoundException;
import com.admin.catalogo.domain.exceptions.Notification;
import com.admin.catalogo.domain.validation.Error;
import com.admin.catalogo.infrastructure.category.models.CreateCategoryAPIInput;
import com.admin.catalogo.infrastructure.category.models.UpdateCategoryAPIInput;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.util.Objects;

import static io.vavr.API.Left;
import static io.vavr.API.Right;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ControllerTest(controllers = CategoryAPI.class)
class CategoryAPITest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private CreateCategoryUseCase createCategoryUseCase;

    @MockBean
    private GetCategoryByIdUseCase getCategoryByIdUseCase;

    @MockBean
    private UpdateCategoryUseCase updateCategoryUseCase;

    @MockBean
    private DeleteCategoryUseCase deleteCategoryUseCase;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void givenAValidCommand_whenCallsCreateCategory_shouldReturnCategoryId() throws Exception {
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;

        final var anApiInput = new CreateCategoryAPIInput(expectedName, expectedDescription, expectedIsActive);

        when(createCategoryUseCase.execute(any()))
                .thenReturn(Right(CreateCategoryOutput.from(CategoryID.from("123"))));

        final var request = post("/categories")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(anApiInput));

        this.mvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/categories/123"))
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id", equalTo("123")));

        verify(createCategoryUseCase, times(1)).execute(argThat(arg ->
            Objects.equals(arg.name(), anApiInput.name())
            && Objects.equals(expectedDescription, anApiInput.description())
            && Objects.equals(expectedIsActive, anApiInput.active())
        ));
    }


    @Test
    public void givenAInvalidName_whenCallsCreateCategory_thenShouldReturnDomainException() throws Exception {
        final String expectedName = null;
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;
        String messageContent = "'name' should be not null";

        final var anApiInput = new CreateCategoryAPIInput(expectedName, expectedDescription, expectedIsActive);

        when(createCategoryUseCase.execute(any()))
                .thenReturn(Left(Notification.create(new Error(messageContent))));

        final var request = post("/categories")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(anApiInput));

        this.mvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(header().string("Location", nullValue()))
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors[0].message", equalTo(messageContent)));

        verify(createCategoryUseCase, times(1)).execute(argThat(arg ->
                Objects.equals(arg.name(), anApiInput.name())
                        && Objects.equals(expectedDescription, anApiInput.description())
                        && Objects.equals(expectedIsActive, anApiInput.active())
        ));
    }

    @Test
    public void givenAInvalidCommand_whenCallsCreateCategory_thenShouldReturnDomainNotification() throws Exception {
        final String expectedName = null;
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;
        String errorMessageContent = "'name' should be not null";

        final var anApiInput = new CreateCategoryAPIInput(expectedName, expectedDescription, expectedIsActive);

        when(createCategoryUseCase.execute(any()))
                .thenThrow(DomainException.with(new Error(errorMessageContent)));

        final var request = post("/categories")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(anApiInput));

        this.mvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(header().string("Location", nullValue()))
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.message", equalTo(errorMessageContent)))
                .andExpect(jsonPath("$.errors[0].message", equalTo(errorMessageContent)));

        verify(createCategoryUseCase, times(1)).execute(argThat(arg ->
                Objects.equals(arg.name(), anApiInput.name())
                        && Objects.equals(expectedDescription, anApiInput.description())
                        && Objects.equals(expectedIsActive, anApiInput.active())
        ));
    }

    @Test
    void givenAValidId_whenCallsGetCategoryById_shouldBeOK() throws Exception {
        //given
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;

        Category aCategory = Category.newCategory(expectedName, expectedDescription);
        final var expectedId = aCategory.getId().getValue();

        when(getCategoryByIdUseCase.execute(any()))
                .thenReturn(CategoryOutput.from(aCategory));

        //when
        final var request = get("/categories/{id}", expectedId);

        final var response = this.mvc.perform(request)
                .andDo(MockMvcResultHandlers.print());

        //then
        response.andExpect(status().isOk())
                        .andExpect(jsonPath("$.id", equalTo(expectedId)))
                        .andExpect(jsonPath("$.name", equalTo(expectedName)))
                        .andExpect(jsonPath("$.description", equalTo(expectedDescription)))
                        .andExpect(jsonPath("$.is_active", equalTo(expectedIsActive)))
                        .andExpect(jsonPath("$.created_at", equalTo(aCategory.getCreatedAt().toString())))
                        .andExpect(jsonPath("$.updated_at", equalTo(aCategory.getUpdatedAt().toString())))
                        .andExpect(jsonPath("$.deleted_at", equalTo(aCategory.getDeletedAt())))
        ;

        verify(getCategoryByIdUseCase, times(1)).execute(eq(expectedId));
    }

    @Test
    void givenAValidId_whenCallsDeleteCategory_shouldReturnNotFound() throws Exception {
        //given
        final var expectedId = CategoryID.from("123");
        final var expectedIdValue = expectedId.getValue();
        final var expectedErrorMessage = "Category with ID %s was not found".formatted(expectedIdValue);

        when(getCategoryByIdUseCase.execute(any()))
                .thenThrow(NotFoundException.with(Category.class, expectedId));

        //when
        final var request = get("/categories/{id}", expectedIdValue);

        final var response = this.mvc.perform(request).andDo(MockMvcResultHandlers.print());

        //then
        response.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", equalTo(expectedErrorMessage)));
    }

    @Test
    public void givenAValidCommand_whenCallsUpdateCategory_shouldReturnCategoryId() throws Exception {
        //given
        final var expectedId = "123";
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;

        when(updateCategoryUseCase.execute(any()))
                .thenReturn(Right(UpdateCategoryOutput.from(expectedId)));

        final var aCommand =
                new UpdateCategoryAPIInput(expectedName, expectedDescription, expectedIsActive);

        //when
        final var request = put("/categories/{id}", expectedId)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(this.objectMapper.writeValueAsString(aCommand));

        final var response = this.mvc.perform(request)
                .andDo(MockMvcResultHandlers.print());

        //then
        response.andExpect(status().isOk())
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id", equalTo(expectedId)))
        ;

        verify(updateCategoryUseCase, times(1)).execute(argThat(cmd -> Objects.equals(
                expectedName, cmd.name())
                && Objects.equals(expectedDescription, cmd.description())
                && Objects.equals(expectedIsActive, cmd.isActive())));
    }

    @Test
    public void givenAInvalidId_whenCallsUpdateCategory_thenShouldReturnDomainException() throws Exception {
        //given
        final var expectedId = "not-found";
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;

        final var expectedErrorMessage = "Category with ID not-found was not found";

        when(updateCategoryUseCase.execute(any()))
                .thenThrow(NotFoundException.with(Category.class, CategoryID.from(expectedId)));

        final var aCommand =
                new UpdateCategoryAPIInput(expectedName, expectedDescription, expectedIsActive);

        //when
        final var request = put("/categories/{id}", expectedId)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(this.objectMapper.writeValueAsString(aCommand));

        final var response = this.mvc.perform(request)
                .andDo(MockMvcResultHandlers.print());

        //then
        response.andExpect(status().isNotFound())
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.message", equalTo(expectedErrorMessage)));

        verify(updateCategoryUseCase, times(1)).execute(argThat(cmd -> Objects.equals(
                expectedName, cmd.name())
                && Objects.equals(expectedDescription, cmd.description())
                && Objects.equals(expectedIsActive, cmd.isActive())));
    }

    @Test
    public void givenAInvalidName_whenCallsUpdateCategory_thenShouldReturnDomainException() throws Exception {
        //given
        final var expectedId = "123";
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;

        final var expectedErrorMessage = "'name' should not be null";

        when(updateCategoryUseCase.execute(any()))
                .thenReturn(Left(Notification.create(new Error(expectedErrorMessage))));

        final var aCommand =
                new UpdateCategoryAPIInput(expectedName, expectedDescription, expectedIsActive);

        //when
        final var request = put("/categories/{id}", expectedId)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(this.objectMapper.writeValueAsString(aCommand));

        final var response = this.mvc.perform(request)
                .andDo(MockMvcResultHandlers.print());

        //then
        response.andExpect(status().isUnprocessableEntity())
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors[0].message", equalTo(expectedErrorMessage)))
        ;

        verify(updateCategoryUseCase, times(1)).execute(argThat(cmd -> Objects.equals(
                expectedName, cmd.name())
                && Objects.equals(expectedDescription, cmd.description())
                && Objects.equals(expectedIsActive, cmd.isActive())));
    }

    @Test
    void givenAValidId_whenCallsDeleteCategory_shouldBeOK() throws Exception {
        //given
        final var expectedId = "123";
        doNothing().when(deleteCategoryUseCase).execute(any());

        //when
        final var request = delete("/categories/{id}", expectedId);
        final var response = this.mvc.perform(request).andDo(MockMvcResultHandlers.print());

        //then
        response.andExpect(status().isNoContent());
        verify(deleteCategoryUseCase, times(1)).execute(expectedId);
    }
}