package com.mb.inventorymanagementservice.exception;

public class InvalidParameterException extends BaseException {

    public InvalidParameterException(String message) {
        super(InventoryManagementServiceErrorCode.INVALID_VALUE, message);
    }
}
