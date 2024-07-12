package com.mb.inventorymanagementservice.exception;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class ErrorResponse implements Serializable {

    private String errorCode;
    private String message;

    @JsonCreator
    public ErrorResponse(@JsonProperty("errorCode") String errorCode, @JsonProperty("message") String message) {
        this.errorCode = errorCode;
        this.message = message;
    }
}
