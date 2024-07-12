package com.mb.inventorymanagementservice.api.controller;

import com.mb.inventorymanagementservice.api.request.ApiUserAuthRequest;
import com.mb.inventorymanagementservice.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody ApiUserAuthRequest apiUserAuthRequest) {
        log.info("Received a request to authenticate user. authenticateUser - ApiUserAuthRequest: {}", apiUserAuthRequest);
        return ResponseEntity.ok(authService.getJwtResponseResponseEntity(apiUserAuthRequest));
    }
}
