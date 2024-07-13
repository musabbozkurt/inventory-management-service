package com.mb.inventorymanagementservice.mapper;

import com.mb.inventorymanagementservice.api.request.ApiProductRequest;
import com.mb.inventorymanagementservice.api.response.ApiProductResponse;
import com.mb.inventorymanagementservice.base.BaseUnitTest;
import com.mb.inventorymanagementservice.data.entity.Product;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ProductMapperTest extends BaseUnitTest {

    ProductMapper productMapper = Mappers.getMapper(ProductMapper.class);

    @Test
    void map_ProductToApiProductResponse_ShouldSucceed() {
        // arrange
        Product product = getProduct();

        // act
        ApiProductResponse result = productMapper.map(product);

        // assertion
        assertEquals(product.getName(), result.getName());
        assertEquals(product.getDescription(), result.getDescription());
        assertEquals(product.getPrice(), result.getPrice());
    }

    @Test
    void map_ListOfProductToListOfApiProductResponse_ShouldSucceed() {
        // arrange
        List<Product> apiProductRequest = getProducts();

        // act
        List<ApiProductResponse> result = productMapper.map(apiProductRequest);

        // assertion
        assertEquals(apiProductRequest.getFirst().getName(), result.getFirst().getName());
        assertEquals(apiProductRequest.getFirst().getDescription(), result.getFirst().getDescription());
        assertEquals(apiProductRequest.getFirst().getPrice(), result.getFirst().getPrice());
        assertEquals(apiProductRequest.getFirst().getId(), result.getFirst().getId());
    }

    @Test
    void map_ApiProductRequestToProduct_ShouldSucceed() {
        // arrange
        ApiProductRequest apiProductRequest = getApiProductRequest();

        // act
        Product result = productMapper.map(apiProductRequest);

        // assertion
        assertEquals(apiProductRequest.getName(), result.getName());
        assertEquals(apiProductRequest.getDescription(), result.getDescription());
        assertEquals(apiProductRequest.getPrice(), result.getPrice());
    }
}
