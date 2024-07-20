package com.mb.inventorymanagementservice.exception;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

import static com.mb.inventorymanagementservice.utils.Constants.EMPTY_LIST;

@Data
public class ErrorResponse implements Serializable {

    private String errorCode;
    private String message;

    @JsonUnwrapped
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private ErrorDetail errorDetail;

    @JsonUnwrapped
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<?> params;

    @JsonCreator
    public ErrorResponse(@JsonProperty("errorCode") String errorCode, @JsonProperty("message") String message) {
        this.errorCode = errorCode;
        this.message = message;
    }

    public ErrorResponse(String errorCode, String message, List<?> params) {
        this.errorCode = errorCode;
        this.message = message;
        this.params = params;
    }

    public ErrorResponse(ErrorCode errorCode, String message) {
        this.errorCode = errorCode.getCode();
        this.message = message;
    }

    public ErrorResponse(String errorCode, String message, ErrorDetail errorDetail) {
        this(errorCode, message, EMPTY_LIST);
        this.errorDetail = errorDetail;
    }

    public ErrorResponse(ErrorCode errorCode, String message, ErrorDetail errorDetail) {
        this(errorCode, message);
        this.errorDetail = errorDetail;
    }
}
