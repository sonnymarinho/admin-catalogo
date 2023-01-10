package com.admin.catalogo.infrastructure;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.springframework.core.env.AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME;

class MainTest {

    @Test
    public void testMain() {
        System.setProperty(ACTIVE_PROFILES_PROPERTY_NAME, "test");
        Assertions.assertNotNull(new Main());
        Main.main(new String[]{});
    }

}