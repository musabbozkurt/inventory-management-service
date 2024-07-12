package com.mb.inventorymanagementservice.service;

import com.mb.inventorymanagementservice.data.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {

    Page<User> getAllUsers(Pageable pageable);

    User createUser(User user);

    User getUserById(Long userId);

    User updateUserById(Long userId, User newUser);

    void deleteUserById(Long userId);
}
