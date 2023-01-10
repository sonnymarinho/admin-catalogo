package com.admin.catalogo.infrastructure.category.persistence;

import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.repository.CrudRepository;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.lang.annotation.*;
import java.util.Collection;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@DataJpaTest
@ComponentScan(
        includeFilters = {
                @ComponentScan.Filter(type = FilterType.REGEX, pattern = ".*[MySQLGateway]")
        }
)
@ActiveProfiles("test")
@ExtendWith(MySQLGatewayTest.CleanUpExtension.class)
public @interface MySQLGatewayTest {

    static class CleanUpExtension implements BeforeEachCallback {

        @Override
        public void beforeEach(ExtensionContext context) throws Exception {
            final var repositories = SpringExtension.getApplicationContext(context)
                    .getBeansOfType(CrudRepository.class)
                    .values();

            this.cleanUp(repositories);
        }

        private void cleanUp(Collection<CrudRepository> repositories) {
            repositories.forEach(CrudRepository::deleteAll);
        }
    }
}
