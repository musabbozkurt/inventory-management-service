package com.mb.inventorymanagementservice.integration_tests.api.controller;

import com.mb.inventorymanagementservice.api.request.ApiUserRequest;
import com.mb.inventorymanagementservice.api.response.ApiUserResponse;
import com.mb.inventorymanagementservice.base.BaseUnitTest;
import com.mb.inventorymanagementservice.config.TestSecurityConfig;
import com.mb.inventorymanagementservice.exception.BaseException;
import com.mb.inventorymanagementservice.exception.InventoryManagementServiceErrorCode;
import com.mb.inventorymanagementservice.mapper.UserMapper;
import com.mb.inventorymanagementservice.service.UserService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;

import java.util.Collections;

@ActiveProfiles("test-containers")
@TestMethodOrder(OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = TestSecurityConfig.class)
public class UserControllerIntegrationTests extends BaseUnitTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserService userService;

    @Autowired
    private UserMapper userMapper;

    @Test
    @Order(value = 1)
    void testServiceConnection() {
        Assertions.assertNotNull(userService);
        Assertions.assertNotNull(userMapper);
    }

    @Test
    @Order(value = 2)
    void testGetUsers() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        String response = restTemplate.exchange("/users/", HttpMethod.GET, new HttpEntity<>(headers), String.class).getBody();

        Assertions.assertNotNull(response);
    }

    @Test
    @Order(value = 3)
    void testCreateUser() {
        ApiUserRequest apiUserRequest = getApiUserRequest();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        ApiUserResponse response = restTemplate.postForObject("/users/", new HttpEntity<>(apiUserRequest, headers), ApiUserResponse.class);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(apiUserRequest.getFirstName(), response.getFirstName());
        Assertions.assertEquals(apiUserRequest.getLastName(), response.getLastName());
        Assertions.assertEquals(apiUserRequest.getEmail(), response.getEmail());
    }

    @Test
    @Order(value = 4)
    void testCreateUser_ShouldFail_WhenPhoneNumberOrEmailIsAlreadyExists() {
        ApiUserRequest apiUserRequest = getApiUserRequest2();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        BaseException exception = restTemplate.postForObject("/users/", new HttpEntity<>(apiUserRequest, headers), BaseException.class);

        Assertions.assertNotNull(exception);
        Assertions.assertEquals(InventoryManagementServiceErrorCode.UNEXPECTED_ERROR, exception.getErrorCode());
    }

    @Test
    @Order(value = 5)
    void testGetUserById() {
        ApiUserResponse response = restTemplate.getForObject("/users/1", ApiUserResponse.class);

        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.getId());
        Assertions.assertNotNull(response.getUsername());
        Assertions.assertNotNull(response.getEmail());
    }

    @Test
    @Order(value = 6)
    void testGetUserById_ShouldFail_WhenUserIsNotFound() {
        BaseException exception = restTemplate.getForObject("/users/0", BaseException.class);

        Assertions.assertNotNull(exception);
        Assertions.assertEquals(InventoryManagementServiceErrorCode.USER_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    @Order(value = 7)
    void testUpdateUserById() {
        ApiUserRequest apiUserRequest = getApiUserRequest3();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        ApiUserResponse response = restTemplate.exchange("/users/1", HttpMethod.PUT, new HttpEntity<>(apiUserRequest, headers), ApiUserResponse.class).getBody();

        Assertions.assertNotNull(response);
        Assertions.assertEquals(apiUserRequest.getEmail(), response.getEmail());
    }

    @Test
    @Order(value = 8)
    void testUpdateUserById_ShouldFail_WhenUserIsNotFound() {
        ApiUserRequest apiUserRequest = getApiUserRequest();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        BaseException exception = restTemplate.exchange("/users/0", HttpMethod.PUT, new HttpEntity<>(apiUserRequest, headers), BaseException.class).getBody();

        Assertions.assertNotNull(exception);
        Assertions.assertEquals(InventoryManagementServiceErrorCode.USER_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    @Disabled
    @Order(value = 9)
    void testDeleteUserById() {
        String response = restTemplate.exchange("/users/13", HttpMethod.DELETE, null, String.class).getBody();

        Assertions.assertEquals("User deleted successfully.", response);
    }

    @Test
    @Order(value = 10)
    void testDeleteUserById_ShouldFail_WhenUserIsNotFound() {
        BaseException exception = restTemplate.exchange("/users/0", HttpMethod.DELETE, null, BaseException.class).getBody();

        Assertions.assertNotNull(exception);
        Assertions.assertEquals(InventoryManagementServiceErrorCode.USER_NOT_FOUND, exception.getErrorCode());
    }
}
