package com.mb.inventorymanagementservice.exception;

import java.util.List;

public class UserNotFoundException extends BaseException {

    public UserNotFoundException(List<String> params) {
        super(InventoryManagementServiceErrorCode.USER_NOT_FOUND, params);
    }
}
