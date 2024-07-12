package com.mb.inventorymanagementservice.mapper;

import com.mb.inventorymanagementservice.api.request.ApiCategoryRequest;
import com.mb.inventorymanagementservice.api.response.ApiCategoryResponse;
import com.mb.inventorymanagementservice.base.BaseUnitTest;
import com.mb.inventorymanagementservice.data.entity.Category;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CategoryMapperTest extends BaseUnitTest {

    CategoryMapper categoryMapper = Mappers.getMapper(CategoryMapper.class);

    @Test
    void map_CategoryToApiCategoryResponse_ShouldSucceed() {
        // arrange
        Category category = getCategory();

        // act
        ApiCategoryResponse result = categoryMapper.map(category);

        // assertion
        assertEquals(category.getName(), result.getName());
        assertEquals(category.getDescription(), result.getDescription());
        assertEquals(category.isLiveInMarket(), result.isLiveInMarket());
        assertEquals(category.getProducts().getFirst().getName(), result.getProducts().getFirst().getName());
        assertEquals(category.getProducts().getFirst().getDescription(), result.getProducts().getFirst().getDescription());
        assertEquals(category.getProducts().getFirst().getCurrentPrice(), result.getProducts().getFirst().getCurrentPrice());
    }

    @Test
    void map_ApiCategoryRequestToCategory_ShouldSucceed() {
        // arrange
        ApiCategoryRequest apiCategoryRequest = getApiCategoryRequest();

        // act
        Category result = categoryMapper.map(apiCategoryRequest);

        // assertion
        assertEquals(apiCategoryRequest.getName(), result.getName());
        assertEquals(apiCategoryRequest.getDescription(), result.getDescription());
    }
}
