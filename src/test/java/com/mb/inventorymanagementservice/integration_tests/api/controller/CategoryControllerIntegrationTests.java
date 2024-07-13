package com.mb.inventorymanagementservice.integration_tests.api.controller;

import com.mb.inventorymanagementservice.api.request.ApiCategoryRequest;
import com.mb.inventorymanagementservice.api.request.ApiProductRequest;
import com.mb.inventorymanagementservice.api.request.ApiUserAuthRequest;
import com.mb.inventorymanagementservice.api.response.ApiCategoryResponse;
import com.mb.inventorymanagementservice.api.response.ApiProductResponse;
import com.mb.inventorymanagementservice.api.response.JwtResponse;
import com.mb.inventorymanagementservice.base.BaseUnitTest;
import com.mb.inventorymanagementservice.config.TestRedisConfiguration;
import com.mb.inventorymanagementservice.exception.BaseException;
import com.mb.inventorymanagementservice.exception.InventoryManagementServiceErrorCode;
import org.javamoney.moneta.Money;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ActiveProfiles("test-containers")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = TestRedisConfiguration.class)
public class CategoryControllerIntegrationTests extends BaseUnitTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @BeforeAll
    public void setUp() {
        // Arrange
        ApiUserAuthRequest apiUserAuthRequest = new ApiUserAuthRequest();
        apiUserAuthRequest.setUsername("admin_user");
        apiUserAuthRequest.setPassword("test1234");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<ApiUserAuthRequest> entity = new HttpEntity<>(apiUserAuthRequest, headers);

        // Act
        ResponseEntity<JwtResponse> response = restTemplate.exchange("/auth/signin", HttpMethod.POST, entity, JwtResponse.class);

        JwtResponse jwtResponse = response.getBody();

        // Assertions
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(jwtResponse);
        String token = jwtResponse.getToken();
        assertNotNull(token);

        // Create RestTemplate with custom header interceptor
        restTemplate.getRestTemplate().setInterceptors(Collections.singletonList((request, body, execution) -> {
            request.getHeaders().add("Authorization", "Bearer ".concat(token));
            return execution.execute(request, body);
        }));
    }

    @Test
    @Order(value = 1)
    void testCreateCategory() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        ApiCategoryRequest request = new ApiCategoryRequest();
        request.setName("Book Category");
        request.setDescription("Book Category Description");

        ResponseEntity<ApiCategoryResponse> response = restTemplate.exchange("/categories/", HttpMethod.POST, new HttpEntity<>(request, headers), ApiCategoryResponse.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getId());
        assertEquals(request.getName(), response.getBody().getName());
        assertEquals(request.getDescription(), response.getBody().getDescription());
    }

    @Test
    @Order(value = 2)
    void testGetAllCategories() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        ResponseEntity<String> response = restTemplate.exchange("/categories/", HttpMethod.GET, new HttpEntity<>(headers), String.class);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @Order(value = 3)
    void testGetCategoryByName() {
        ResponseEntity<ApiCategoryResponse> response = restTemplate.getForEntity("/categories/Electronics Category", ApiCategoryResponse.class);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        Assertions.assertEquals("Electronics Category", response.getBody().getName());
    }

    @Test
    @Order(value = 4)
    void testGetCategoryByName_ShouldFail_WhenCategoryIsNotFound() {
        ResponseEntity<BaseException> response = restTemplate.getForEntity("/categories/Book Categoryy", BaseException.class);

        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        Assertions.assertEquals(InventoryManagementServiceErrorCode.CATEGORY_NOT_FOUND, response.getBody().getErrorCode());
    }

    @Test
    @Order(value = 6)
    void testCreateCategory_ShouldFail_WhenCategoryCodeIsAlreadyExists() {
        ApiCategoryRequest apiCategoryRequest = getApiCategoryRequest();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        ResponseEntity<BaseException> response = restTemplate.exchange("/categories/", HttpMethod.POST, new HttpEntity<>(apiCategoryRequest, headers), BaseException.class);

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        Assertions.assertEquals(InventoryManagementServiceErrorCode.ALREADY_EXISTS, response.getBody().getErrorCode());
    }

    @Test
    @Order(value = 7)
    void testDeleteCategory() {
        ResponseEntity<String> response = restTemplate.exchange("/categories/3", HttpMethod.DELETE, null, String.class);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals("Category deleted successfully.", response.getBody());
    }

    @Test
    @Order(value = 8)
    void testDeleteCategory_ShouldFail_WhenCategoryIsNotFound() {
        ResponseEntity<BaseException> response = restTemplate.exchange("/categories/15", HttpMethod.DELETE, null, BaseException.class);

        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        Assertions.assertEquals(InventoryManagementServiceErrorCode.CATEGORY_NOT_FOUND, response.getBody().getErrorCode());
    }

    @Test
    @Order(value = 9)
    void testAddProductToCategory() {
        ApiProductRequest productRequest = new ApiProductRequest();
        productRequest.setName("Art");
        productRequest.setProductCode("Art");
        productRequest.setDescription("Art Product Description");
        productRequest.setAmount(Money.of(BigDecimal.valueOf(6.0), "EUR"));
        productRequest.setQuantity(2000);

        ResponseEntity<ApiCategoryResponse> response = restTemplate.exchange("/categories/Book Category/products", HttpMethod.POST, new HttpEntity<>(productRequest), ApiCategoryResponse.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    @Order(value = 10)
    void testAddProductToCategory_ShouldFail_WhenProductIsNotFound() {
        ApiProductRequest productRequest = new ApiProductRequest();
        productRequest.setName("Perfumes");
        productRequest.setDescription("Perfumes Description");
        productRequest.setAmount(Money.of(BigDecimal.valueOf(100.0), "EUR"));

        ResponseEntity<BaseException> response = restTemplate.exchange("/categories/Beauty Category/products", HttpMethod.POST, new HttpEntity<>(productRequest), BaseException.class);

        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        Assertions.assertEquals(InventoryManagementServiceErrorCode.PRODUCT_NOT_FOUND, response.getBody().getErrorCode());
    }

    @Test
    @Order(value = 11)
    void testGetProductsByCategory() {
        ResponseEntity<List<ApiProductResponse>> response = restTemplate.exchange("/categories/Beauty Category/products", HttpMethod.GET, null, new ParameterizedTypeReference<>() {
        });

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    @Order(value = 12)
    void testGetProductsByCategoryName_ShouldFail_WhenCategoryIsNotFound() {
        ResponseEntity<BaseException> response = restTemplate.getForEntity("/categories/Automotive Category/products", BaseException.class);

        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        Assertions.assertEquals(InventoryManagementServiceErrorCode.CATEGORY_NOT_FOUND, response.getBody().getErrorCode());
    }

    @Test
    @Order(value = 13)
    void testDeleteProductFromCategory() {
        ResponseEntity<ApiCategoryResponse> response = restTemplate.exchange("/categories/Book Category/Art", HttpMethod.DELETE, null, ApiCategoryResponse.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    @Order(value = 14)
    void testDeleteProductFromCategory_ShouldFail_WhenCategoryIsNotFound() {
        ResponseEntity<BaseException> response = restTemplate.exchange("/categories/Automotive Category/BMW", HttpMethod.DELETE, null, BaseException.class);

        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        Assertions.assertEquals(InventoryManagementServiceErrorCode.CATEGORY_NOT_FOUND, response.getBody().getErrorCode());
    }

    @Test
    @Order(value = 15)
    void testDeleteProductFromCategory_ShouldFail_WhenProductIsNotFound() {
        ResponseEntity<BaseException> response = restTemplate.exchange("/categories/Beauty Category/Perfumes", HttpMethod.DELETE, null, BaseException.class);

        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        Assertions.assertEquals(InventoryManagementServiceErrorCode.PRODUCT_NOT_FOUND, response.getBody().getErrorCode());
    }
}
