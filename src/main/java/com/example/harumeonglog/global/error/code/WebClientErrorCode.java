package com.example.harumeonglog.global.error.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum WebClientErrorCode implements BaseErrorCode {
    WEB_CLIENT_ERROR_CODE(HttpStatus.INTERNAL_SERVER_ERROR, "WEB_CLIENT500","WebClient 동작 도중 에러 발생")
    ;
    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
