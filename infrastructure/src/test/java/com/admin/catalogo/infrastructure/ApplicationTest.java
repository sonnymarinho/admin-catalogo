package com.admin.catalogo.infrastructure;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ApplicationTest {

    @Test
    public void testMain() {
        Assertions.assertNotNull(new Application());

        Application.main(new String[]{});
    }

}