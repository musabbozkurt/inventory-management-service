package com.mb.inventorymanagementservice.service;

import org.springframework.security.core.Authentication;

public interface TokenStore {

    String getAccessToken(Authentication authentication);

    void storeAccessToken(String token);
}
