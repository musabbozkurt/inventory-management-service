package com.mb.inventorymanagementservice.exception;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@ControllerAdvice
public class RestResponseExceptionHandler {

    @ResponseBody
    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ErrorResponse> handleBaseException(BaseException ex) {
        log.error("BaseException occurred: {}", ex.getErrorCode(), ex);
        return new ResponseEntity<>(new ErrorResponse(ex.getErrorCode().getCode(), ex.getMessage()), ex.getErrorCode().getHttpStatus());
    }

    @ResponseBody
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception ex) {
        log.error("Exception occurred: {}", ExceptionUtils.getStackTrace(ex));
        if (ex instanceof BadCredentialsException) {
            return new ResponseEntity<>(new ErrorResponse(InventoryManagementServiceErrorCode.BAD_CREDENTIALS.getCode(), ex.getMessage()), InventoryManagementServiceErrorCode.BAD_CREDENTIALS.getHttpStatus());
        }
        return new ResponseEntity<>(new ErrorResponse(InventoryManagementServiceErrorCode.UNEXPECTED_ERROR.getCode(), ex.getMessage()), InventoryManagementServiceErrorCode.UNEXPECTED_ERROR.getHttpStatus());
    }
}
