package com.mb.inventorymanagementservice.integration_tests.api.controller;

import com.mb.inventorymanagementservice.api.request.ApiUserAuthRequest;
import com.mb.inventorymanagementservice.api.response.JwtResponse;
import com.mb.inventorymanagementservice.base.BaseUnitTest;
import com.mb.inventorymanagementservice.config.TestRedisConfiguration;
import com.mb.inventorymanagementservice.exception.BaseException;
import com.mb.inventorymanagementservice.exception.InventoryManagementServiceErrorCode;
import com.mb.inventorymanagementservice.utils.JwtUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test-containers")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = TestRedisConfiguration.class)
public class AuthControllerIntegrationTests extends BaseUnitTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private JwtUtils jwtUtils;

    @Test
    public void testAuthenticationUser() {
        // Arrange
        ApiUserAuthRequest request = new ApiUserAuthRequest();
        request.setUsername("admin_user");
        request.setPassword("test1234");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<ApiUserAuthRequest> entity = new HttpEntity<>(request, headers);

        // Act
        ResponseEntity<JwtResponse> response = restTemplate.exchange("/auth/signin", HttpMethod.POST, entity, JwtResponse.class);

        JwtResponse jwtResponse = response.getBody();

        // Assertions
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(jwtResponse);
        String token = jwtResponse.getToken();
        assertNotNull(token);
        String userName = jwtUtils.getUserNameFromJwtToken(token);
        assertEquals(request.getUsername(), userName);
        assertTrue(jwtUtils.validateJwtToken(token));
    }

    @Test
    public void testAuthenticationUser_ShouldFail_ApiUserAuthRequestIsInvalid() {
        // Arrange
        ApiUserAuthRequest request = new ApiUserAuthRequest();
        request.setUsername("example_username");
        request.setPassword("example_password");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<ApiUserAuthRequest> entity = new HttpEntity<>(request, headers);

        // Act
        ResponseEntity<BaseException> response = restTemplate.exchange("/auth/signin", HttpMethod.POST, entity, BaseException.class);

        BaseException baseException = response.getBody();

        // Assertions
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertNotNull(baseException);
        assertEquals(InventoryManagementServiceErrorCode.BAD_CREDENTIALS, baseException.getErrorCode());
    }
}
