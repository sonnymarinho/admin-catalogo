package com.admin.catalogo.application;

import com.admin.catalogo.domain.category.Category;

public abstract class UnityUseCase<IN> {

    public abstract void execute(IN anIn);
}