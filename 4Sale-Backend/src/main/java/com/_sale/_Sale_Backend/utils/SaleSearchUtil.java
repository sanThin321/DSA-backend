package com._sale._Sale_Backend.utils;

import com._sale._Sale_Backend.model.Sale;

import java.util.ArrayList;
import java.util.List;

public class SaleSearchUtil {

    // Bubble Sort to sort sales by customerName
    public static void bubbleSortByName(List<Sale> sales) {
        int n = sales.size();
        // Traverse through all sales
        for (int i = 0; i < n - 1; i++) {
            // Last i elements are already sorted
            for (int j = 0; j < n - i - 1; j++) {
                // Compare the adjacent elements
                if (sales.get(j).getCustomerName().compareToIgnoreCase(sales.get(j + 1).getCustomerName()) > 0) {
                    // Swap sales[j] and sales[j+1] if they are in the wrong order
                    Sale temp = sales.get(j);
                    sales.set(j, sales.get(j + 1));
                    sales.set(j + 1, temp);
                }
            }
        }
    }

    // Linear Search for sales by customerName
    public static List<Sale> linearSearchByName(List<Sale> sales, String query) {
        List<Sale> result = new ArrayList<>();
        for (Sale sale : sales) {
            // Exact or partial match (case-insensitive)
            if (sale.getCustomerName().toLowerCase().contains(query.toLowerCase())) {
                result.add(sale);
            }
        }
        return result;
    }
}
