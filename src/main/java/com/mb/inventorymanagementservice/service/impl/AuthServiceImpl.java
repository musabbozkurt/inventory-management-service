package com.mb.inventorymanagementservice.service.impl;

import com.mb.inventorymanagementservice.api.request.ApiUserAuthRequest;
import com.mb.inventorymanagementservice.api.response.JwtResponse;
import com.mb.inventorymanagementservice.service.AuthService;
import com.mb.inventorymanagementservice.service.TokenStore;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final TokenStore tokenStore;

    @Override
    public JwtResponse getJwtResponseResponseEntity(ApiUserAuthRequest apiUserAuthRequest) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(apiUserAuthRequest.getUsername(), apiUserAuthRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenStore.getAccessToken(authentication);

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        return new JwtResponse(jwt, userDetails.getUsername(), roles);
    }
}
