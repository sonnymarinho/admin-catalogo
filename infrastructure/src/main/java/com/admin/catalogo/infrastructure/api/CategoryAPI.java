package com.admin.catalogo.infrastructure.api;

import com.admin.catalogo.domain.pagination.Pagination;
import com.admin.catalogo.infrastructure.category.models.CategoryAPIOutput;
import com.admin.catalogo.infrastructure.category.models.CreateCategoryAPIInput;
import com.admin.catalogo.infrastructure.category.models.UpdateCategoryAPIInput;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Categories")
@RequestMapping("/categories")
public interface CategoryAPI {

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new category")
    @ApiResponses({
             @ApiResponse(responseCode = "201", description = "Created successfully")
            ,@ApiResponse(responseCode = "422", description = "A validation error was thrown")
            ,@ApiResponse(responseCode = "500", description = "An internal server error was thrown")
    })
    ResponseEntity<?> createCategory(@RequestBody CreateCategoryAPIInput input);

    @GetMapping
    @Operation(summary = "List all categories paginated")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Category created successfully")
            ,@ApiResponse(responseCode = "422", description = "A invalid parameter was received")
            ,@ApiResponse(responseCode = "500", description = "An internal server error was thrown")
    })
    Pagination<?> listCategories(
             @RequestParam(name = "search", required = false, defaultValue = "") final String search
            ,@RequestParam(name = "page", required = false, defaultValue = "0") final Integer page
            ,@RequestParam(name = "perPage", required = false, defaultValue = "10") final Integer perPage
            ,@RequestParam(name = "sort", required = false, defaultValue = "name") final String sort
            ,@RequestParam(name = "dir", required = false, defaultValue = "asc") final String dir
    );

    @GetMapping(
            value = "{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(summary = "Get a category by it's identifier")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Created retrieved successfully")
            ,@ApiResponse(responseCode = "404", description = "Category was not found")
            ,@ApiResponse(responseCode = "500", description = "An internal server error was thrown")
    })
    CategoryAPIOutput getById(@PathVariable(name = "id") String id);

    @PutMapping(
            value = "{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(summary = "Update a category by it's identifier")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Category updated successfully")
            ,@ApiResponse(responseCode = "404", description = "Category was not found")
            ,@ApiResponse(responseCode = "500", description = "An internal server error was thrown")
    })
    ResponseEntity<?> updateById(@PathVariable(name = "id") String id,
                                 @RequestBody UpdateCategoryAPIInput anInput);

    @DeleteMapping(
            value = "{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a category by it's identifier")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Category deleted successfully")
            ,@ApiResponse(responseCode = "404", description = "Category was not found")
            ,@ApiResponse(responseCode = "500", description = "An internal server error was thrown")
    })
    void deleteById(@PathVariable(name = "id") String id);
}
