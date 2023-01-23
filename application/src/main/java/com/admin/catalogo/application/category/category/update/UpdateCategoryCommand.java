package com.admin.catalogo.application.category.category.update;

import java.util.Optional;

public record UpdateCategoryCommand (
        String id,
        String name,
        String description,
        Boolean isActive
) {

    public static UpdateCategoryCommand with(
            final String anId,
            final String aName,
            final String aDescription,
            final Boolean isActive
    ) {
        final var validIsActive = Optional.ofNullable(isActive).orElse(true);
        return new UpdateCategoryCommand(anId, aName, aDescription, validIsActive);
    }
}
