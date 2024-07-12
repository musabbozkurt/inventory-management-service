package com.mb.inventorymanagementservice.base;

import com.mb.inventorymanagementservice.api.request.ApiCategoryRequest;
import com.mb.inventorymanagementservice.api.request.ApiProductRequest;
import com.mb.inventorymanagementservice.api.request.ApiUserRequest;
import com.mb.inventorymanagementservice.data.entity.Category;
import com.mb.inventorymanagementservice.data.entity.Product;
import com.mb.inventorymanagementservice.data.entity.User;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

public abstract class BaseUnitTest {

    public static User getUser() {
        User user = new User();
        user.setId(2L);
        user.setCreatedDateTime(OffsetDateTime.now());
        user.setModifiedDateTime(OffsetDateTime.now());
        user.setFirstName("Jack");
        user.setLastName("Hack");
        user.setUsername("jack_hack");
        user.setEmail("jack.hack@gmail.com");
        user.setPhoneNumber("1234567890");
        return user;
    }

    public static ApiUserRequest getApiUserRequest() {
        ApiUserRequest apiUserRequest = new ApiUserRequest();
        apiUserRequest.setFirstName("Jack");
        apiUserRequest.setLastName("Hack");
        apiUserRequest.setUsername("jack_hack");
        apiUserRequest.setPassword("test1234");
        apiUserRequest.setEmail("jack.hack@gmail.com");
        apiUserRequest.setPhoneNumber("1234567899");
        return apiUserRequest;
    }

    public static ApiUserRequest getApiUserRequest2() {
        ApiUserRequest apiUserRequest = new ApiUserRequest();
        apiUserRequest.setFirstName("Jack");
        apiUserRequest.setLastName("Hack");
        apiUserRequest.setUsername("jack_hack");
        apiUserRequest.setPassword("test1234");
        apiUserRequest.setEmail("jack.hack@gmail.com");
        apiUserRequest.setPhoneNumber("1234567890");
        return apiUserRequest;
    }

    public static ApiUserRequest getApiUserRequest3() {
        ApiUserRequest apiUserRequest = new ApiUserRequest();
        apiUserRequest.setEmail("jack.hack.new@gmail.com");
        return apiUserRequest;
    }

    public static Product getProduct() {
        Product product = new Product();
        product.setName("IPHONE 13");
        product.setProductCode("IPHONE_13");
        product.setDescription("IPHONE 13 Description");
        product.setCurrentPrice(BigDecimal.valueOf(22.12));
        product.setQuantity(10);
        return product;
    }

    public static List<Product> getProducts() {
        return List.of(getProduct());
    }

    public static ApiProductRequest getApiProductRequest() {
        return ApiProductRequest.builder().name("IPHONE 15").productCode("IPHONE_15").description("IPHONE 15 Description").currentPrice(BigDecimal.valueOf(2850)).currency("EUR").quantity(10).build();
    }

    public static ApiProductRequest getApiProductRequest2() {
        return ApiProductRequest.builder().name("IPHONE 13").productCode("IPHONE_13").description("IPHONE 13 Description").currentPrice(BigDecimal.valueOf(2500)).quantity(10).build();
    }

    public static ApiCategoryRequest getApiCategoryRequest() {
        ApiCategoryRequest apiCategoryRequest = new ApiCategoryRequest();
        apiCategoryRequest.setName("Electronics Category");
        apiCategoryRequest.setDescription("Electronics Category Description");
        return apiCategoryRequest;
    }

    public static Category getCategory() {
        Category category = new Category();
        category.setName("Beauty Category");
        category.setDescription("Beauty Category Description");
        category.setLiveInMarket(false);
        category.setProducts(List.of(getProduct()));
        return category;
    }
}
