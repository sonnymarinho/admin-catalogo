package com.admin.catalogo.application.category.update;

import com.admin.catalogo.application.UseCase;
import com.admin.catalogo.domain.exceptions.Notification;
import io.vavr.control.Either;

public abstract class UpdateCategoryUseCase extends UseCase<UpdateCategoryCommand, Either<Notification, UpdateCategoryOutput>> {

}
