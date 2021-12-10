package com.learnup.enums;

import lombok.Getter;

public enum CategoryType {
    FOOD(1, "Food"),
    ELECTRONIC(2, "Electronic"),
    FURNITURE(3, "Furniture");

    @Getter
    int id;
    @Getter
    String categoryName;
    CategoryType(int id, String categoryName) {
        this.id = id;
        this.categoryName = categoryName;
    }
}
