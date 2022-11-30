package com.admin.catalogo.domain.category;

import com.admin.catalogo.domain.validation.Error;
import com.admin.catalogo.domain.validation.ValidationHandler;
import com.admin.catalogo.domain.validation.Validator;

import java.util.Objects;

public class CategoryValidator extends Validator {

    public static final int NAME_MAX_LENGTH = 255;
    public static final int NAME_MIN_LENGTH = 3;
    private final Category category;

    public CategoryValidator(final Category aCategory,
                             final ValidationHandler aHandler) {
        super(aHandler);
        this.category = aCategory;
    }

    @Override
    public void validate() {
        checkNameConstraints();
        checkDescriptionConstraints();
    }

    private void checkDescriptionConstraints() {
        final var description = this.category.getDescription();

        if (Objects.isNull(description)) {
            this.validationHandler().append(new Error("'description' should not be null"));
            return;
        }

        if (description.isEmpty()) {
            this.validationHandler().append(new Error("'description' should not be empty"));
            return;
        }

        if (description.length() > 255 || description.length() < 3) {
            this.validationHandler().append(new Error("'description' must be between 3 and 255 characters"));
        }
    }

    private void checkNameConstraints() {
        final var name = this.category.getName();

        if (Objects.isNull(name)) {
            this.validationHandler().append(new Error("'name' should not be null"));
            return;
        }

        if (name.isBlank()) {
            this.validationHandler().append(new Error("'name' should not be blank"));
            return;
        }

        int nameLength = name.trim().length();
        if (nameLength > NAME_MAX_LENGTH || nameLength < NAME_MIN_LENGTH) {
            this.validationHandler().append(new Error("'name' must be between 3 and 255 characters"));
        }
    }
}
