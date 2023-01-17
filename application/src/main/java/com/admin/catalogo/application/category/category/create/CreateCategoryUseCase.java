package com.admin.catalogo.application.category.category.create;

import com.admin.catalogo.application.category.UseCase;
import com.admin.catalogo.domain.exceptions.Notification;
import io.vavr.control.Either;

public abstract class CreateCategoryUseCase
        extends UseCase<CreateCategoryCommand, Either<Notification, CreateCategoryOutput>> {

}
