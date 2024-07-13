package com.mb.inventorymanagementservice.integration_tests.api.controller;

import com.mb.inventorymanagementservice.api.request.ApiProductRequest;
import com.mb.inventorymanagementservice.api.request.ApiUserAuthRequest;
import com.mb.inventorymanagementservice.api.response.ApiProductResponse;
import com.mb.inventorymanagementservice.api.response.JwtResponse;
import com.mb.inventorymanagementservice.base.BaseUnitTest;
import com.mb.inventorymanagementservice.config.TestRedisConfiguration;
import com.mb.inventorymanagementservice.exception.BaseException;
import com.mb.inventorymanagementservice.exception.InventoryManagementServiceErrorCode;
import com.mb.inventorymanagementservice.mapper.ProductMapper;
import com.mb.inventorymanagementservice.service.ProductService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ActiveProfiles("test-containers")
@TestMethodOrder(OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = TestRedisConfiguration.class)
public class ProductControllerIntegrationTests extends BaseUnitTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductMapper productMapper;

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
    void testConnectionToDatabase() {
        Assertions.assertNotNull(productService);
        Assertions.assertNotNull(productMapper);
    }

    @Test
    @Order(value = 2)
    void testGetAllProducts() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        ResponseEntity<String> response = restTemplate.exchange("/products/", HttpMethod.GET, new HttpEntity<>(headers), String.class);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @Order(value = 3)
    void testGetProductById() {
        ResponseEntity<ApiProductResponse> response = restTemplate.getForEntity("/products/Novel", ApiProductResponse.class);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        Assertions.assertEquals("Novel", response.getBody().getName());
    }

    @Test
    @Order(value = 4)
    void testGetProductById_ShouldFail_WhenProductIsNotFound() {
        ResponseEntity<BaseException> response = restTemplate.getForEntity("/products/Novell", BaseException.class);

        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        Assertions.assertEquals(InventoryManagementServiceErrorCode.PRODUCT_NOT_FOUND, response.getBody().getErrorCode());
    }

    @Test
    @Order(value = 5)
    void testCreateProduct() {
        ApiProductRequest apiProductRequest = getApiProductRequest();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        ResponseEntity<ApiProductResponse> response = restTemplate.exchange("/products/", HttpMethod.POST, new HttpEntity<>(apiProductRequest, headers), ApiProductResponse.class);

        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(apiProductRequest.getName(), response.getBody().getName());
        Assertions.assertEquals(apiProductRequest.getDescription(), response.getBody().getDescription());
        Assertions.assertEquals(apiProductRequest.getAmount(), response.getBody().getAmount());
    }

    @Test
    @Order(value = 6)
    void testCreateProduct_ShouldFail_WhenProductCodeIsAlreadyExists() {
        ApiProductRequest apiProductRequest = getApiProductRequest2();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        ResponseEntity<BaseException> response = restTemplate.exchange("/products/", HttpMethod.POST, new HttpEntity<>(apiProductRequest, headers), BaseException.class);

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        Assertions.assertEquals(InventoryManagementServiceErrorCode.ALREADY_EXISTS, response.getBody().getErrorCode());
    }

    @Test
    @Order(value = 7)
    void testDeleteProduct() {
        ResponseEntity<String> response = restTemplate.exchange("/products/6", HttpMethod.DELETE, null, String.class);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals("Product deleted successfully.", response.getBody());
    }

    @Test
    @Order(value = 8)
    void testDeleteProduct_ShouldFail_WhenProductIsNotFound() {
        ResponseEntity<BaseException> response = restTemplate.exchange("/products/1", HttpMethod.DELETE, null, BaseException.class);

        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        Assertions.assertEquals(InventoryManagementServiceErrorCode.PRODUCT_NOT_FOUND, response.getBody().getErrorCode());
    }

    @Test
    @Order(value = 9)
    void testGetQuantityByProductCode() {
        ResponseEntity<Integer> response = restTemplate.getForEntity("/products/IPHONE_13/quantity", Integer.class);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        Assertions.assertEquals(10, response.getBody());
    }

    @Test
    @Order(value = 10)
    void testGetQuantityByProductCode_ShouldReturnNull_WhenProductCodeIsNotFound() {
        ResponseEntity<Integer> response = restTemplate.getForEntity("/products/Novelll/quantity", Integer.class);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNull(response.getBody());
    }
}
