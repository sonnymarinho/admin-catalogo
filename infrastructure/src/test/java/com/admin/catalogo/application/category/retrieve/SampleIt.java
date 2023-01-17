package com.admin.catalogo.application.category.retrieve;

import com.admin.catalogo.IntegrationTest;
import com.admin.catalogo.application.category.category.create.CreateCategoryUseCase;
import com.admin.catalogo.infrastructure.category.CategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@IntegrationTest
public class SampleIt {

    @Autowired
    CreateCategoryUseCase createCategoryUseCase;


    @Autowired
    CategoryRepository categoryRepository;

    @Test
    public void testInject() {
        Assertions.assertNotNull(createCategoryUseCase);
        Assertions.assertNotNull(categoryRepository);
    }
}
