package ru.matmex.subscription.utils;

import ru.matmex.subscription.entities.Category;

import java.util.ArrayList;

public class CategoryBuilder {
    private CategoryBuilder() {
    }

    public static CategoryBuilder anCategory() {
        return new CategoryBuilder();
    }

    public Category defaultCategory() {
        return new Category("test",new ArrayList<>(),UserBuilder.anUser().defaultUser());
    }
}
