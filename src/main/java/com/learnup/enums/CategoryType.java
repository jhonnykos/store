package com.learnup.enums;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

public enum CategoryType {
    FOOD(1, "Food"),
    ELECTRONIC(2, "Electronic"),
    FURNITURE(3, "Furniture");

    private static final Map<String, CategoryType> mapCategoryNames;

    static {
        mapCategoryNames = new HashMap<String, CategoryType>();
        for (CategoryType c : CategoryType.values()) {
            mapCategoryNames.put(c.categoryName, c);
        }
    }

    public static CategoryType findByCategoryTitle(String c) {
        return mapCategoryNames.get(c);
    }

    @Getter
    private int id;
    @Getter
    private String categoryName;

    CategoryType(int id, String categoryName) {
        this.id = id;
        this.categoryName = categoryName;
    }
}
