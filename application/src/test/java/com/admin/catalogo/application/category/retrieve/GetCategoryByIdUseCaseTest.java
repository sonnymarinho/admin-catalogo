package com.admin.catalogo.application.category.retrieve;

import com.admin.catalogo.application.category.delete.DefaultDeleteCategoryUseCase;
import com.admin.catalogo.domain.category.CategoryGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GetCategoryByIdUseCaseTest {

    private DefaultDeleteCategoryUseCase useCase;

    @Mock
    CategoryGateway categoryGateway;


    @BeforeEach
    void cleanUp() {
        Mockito.reset(categoryGateway);
    }

}