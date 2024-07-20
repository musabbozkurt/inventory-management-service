package com.mb.inventorymanagementservice.exception;

public class UserNotFoundException extends BaseException {

    public UserNotFoundException(String message) {
        super(InventoryManagementServiceErrorCode.USER_NOT_FOUND, message);
    }
}
