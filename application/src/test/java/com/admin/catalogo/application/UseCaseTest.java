package com.admin.catalogo.application;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UseCaseTest {

    @Test
    public void testCreateUseCase() {
        Assertions.assertNotNull(new UseCase().execute());
    }

}