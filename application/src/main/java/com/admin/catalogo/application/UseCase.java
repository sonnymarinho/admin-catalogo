package com.admin.catalogo.application;

import com.admin.catalogo.application.category.create.CreateCategoryOutput;
import com.admin.catalogo.domain.category.Category;

public abstract class UseCase<IN, OUT> {

    public abstract OUT execute(IN anIn);
}