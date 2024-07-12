package com.mb.inventorymanagementservice.service;

import com.mb.inventorymanagementservice.data.entity.Category;
import com.mb.inventorymanagementservice.data.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CategoryService {

    Page<Category> getAllCategories(Pageable pageable);

    Category findByName(String name);

    Category createCategory(Category category);

    Category updateCategoryById(Long categoryId, Category newCategory);

    void deleteCategoryById(Long categoryId);

    Category addProductToCategory(String name, Product product);

    Category deleteProductFromCategory(String name, String productName);
}
