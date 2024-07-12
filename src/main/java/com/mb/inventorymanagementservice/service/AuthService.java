package com.mb.inventorymanagementservice.service;

import com.mb.inventorymanagementservice.api.request.ApiUserAuthRequest;
import com.mb.inventorymanagementservice.api.response.JwtResponse;

public interface AuthService {

    JwtResponse getJwtResponseResponseEntity(ApiUserAuthRequest apiUserAuthRequest);
}
