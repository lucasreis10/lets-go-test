package com.catalogo.infrastructure;

import org.junit.jupiter.api.Assertions;
import org.springframework.core.env.AbstractEnvironment;

public class MainTest {

    public void mainTest() {
        System.setProperty(AbstractEnvironment.DEFAULT_PROFILES_PROPERTY_NAME, "test");
        Assertions.assertNotNull(new Main());
        Main.main(new String[]{});
    }

}
