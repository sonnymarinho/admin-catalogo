package com.admin.catalogo.domain.category;

import com.admin.catalogo.domain.AggregateRoot;
import com.admin.catalogo.domain.validation.ValidationHandler;

import java.time.Instant;
import java.util.Objects;

public class Category extends AggregateRoot<CategoryID> implements Cloneable {
    private String name;
    private String description;
    private boolean active;
    private Instant createdAt;
    private Instant updatedAt;
    private Instant deletedAt;

    public Category(final CategoryID anId,
                    final String aName,
                    final String aDescription,
                    final boolean isActive,
                    final Instant aCreationDate,
                    final Instant aUpdateDate,
                    final Instant aDeleteDate) {
        super(anId);
        this.name = aName;
        this.description = aDescription;
        this.active = isActive;
        this.createdAt = Objects.requireNonNull(aCreationDate, "'createdAt' should not be null");
        this.updatedAt = Objects.requireNonNull(aUpdateDate, "'updatedAt' should not be null");
        this.deletedAt = aDeleteDate;
    }

    public static Category newCategory(final String aName,
                                       final String aDescription,
                                       final boolean isActive) {
        final var id = CategoryID.unique();
        var now = Instant.now();
        Instant aDeleteDate = isActive ? null : now;
        return new Category(id, aName, aDescription, isActive, now, now, aDeleteDate);
    }

    public static Category newCategory(final String aName,
                                       final String aDescription) {
        return Category.newCategory(aName, aDescription, true);
    }

    public static Category newCategory(final Category aCategory) {
        return aCategory.clone();
    }

    public static Category with(
            final CategoryID anId,
            final String aName,
            final String aDescription,
            final boolean isActive,
            final Instant aCreationDate,
            final Instant aUpdateDate,
            final Instant aDeleteDate) {

        return new Category(
                anId,
                aName,
                aDescription,
                isActive,
                aCreationDate,
                aUpdateDate,
                aDeleteDate
        );
    }
    @Override
    public void validate(final ValidationHandler handler) {
        new CategoryValidator(this, handler).validate();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Instant getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(Instant deletedAt) {
        this.deletedAt = deletedAt;
    }

    public Category deactivate() {
        if (Objects.isNull(getDeletedAt())) this.deletedAt = Instant.now();

        this.active = false;
        this.updatedAt = Instant.now();

        return this;
    }

    public Category activate() {
        this.deletedAt = null;
        this.active = true;
        this.updatedAt = Instant.now();

        return this;
    }

    public Category update(final String aName,
                       final String aDescription,
                       final boolean isActive) {
        this.name = aName;
        this.description = aDescription;

        if(isActive) activate(); else deactivate();

        this.updatedAt = Instant.now();

        return this;
    }

    @Override
    public Category clone() {
        try {
            Category clone = (Category) super.clone();
            // TODO: copy mutable state here, so the clone can't change the internals of the original
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}