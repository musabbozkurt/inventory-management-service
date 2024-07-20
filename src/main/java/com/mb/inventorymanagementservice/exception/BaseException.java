package com.mb.inventorymanagementservice.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

import static com.mb.inventorymanagementservice.utils.Constants.EMPTY_LIST;

@Data
@ToString
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class BaseException extends RuntimeException implements Serializable {

    private ErrorCode errorCode;
    private String message;
    private ErrorDetail errorDetail;
    private List<?> params;

    public BaseException(ErrorCode errorCode) {
        super();
        this.errorCode = errorCode;
        this.params = EMPTY_LIST;
    }

    public BaseException(ErrorCode errorCode, List<?> params) {
        super();
        this.errorCode = errorCode;
        this.params = params;
    }

    public BaseException(ErrorCode errorCode, Throwable cause) {
        super(cause);
        this.errorCode = errorCode;
        this.params = EMPTY_LIST;
    }

    public BaseException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
        this.params = EMPTY_LIST;
    }

    public BaseException(ErrorCode errorCode, String message, List<?> params) {
        super(message);
        this.errorCode = errorCode;
        this.params = params;
    }

    public BaseException(ErrorCode errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
        this.params = EMPTY_LIST;
    }
}
