package com._sale._Sale_Backend.utils;

import com._sale._Sale_Backend.model.Sale;

import java.util.List;

public class BubbleSort {

    // Bubble Sort to sort a list of Sale objects by customerName
    public static List<Sale> bubbleSort(List<Sale> list) {
        int n = list.size();
        // Traverse through all elements
        for (int i = 0; i < n - 1; i++) {
            // Last i elements are already sorted
            for (int j = 0; j < n - i - 1; j++) {
                // Compare adjacent elements using compareTo from Sale class
                if (list.get(j).compareTo(list.get(j + 1)) > 0) {
                    // Swap elements if they are in the wrong order
                    Sale temp = list.get(j);
                    list.set(j, list.get(j + 1));
                    list.set(j + 1, temp);
                }
            }
        }
        return list;
    }
}
