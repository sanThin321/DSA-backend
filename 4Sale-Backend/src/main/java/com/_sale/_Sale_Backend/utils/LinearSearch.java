package com._sale._Sale_Backend.utils;

import java.util.List;

public class LinearSearch {
    // Generic method for linear search
    public static <T> T linearSearch(List<T> list, T target) {
        for (T item : list) {
            if (item.equals(target)) {
                return item;  // Return the item if it's found
            }
        }
        return null;  // Return null if not found
    }
}
