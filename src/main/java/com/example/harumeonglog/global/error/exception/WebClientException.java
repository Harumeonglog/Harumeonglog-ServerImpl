package com.example.harumeonglog.global.error.exception;

import com.example.harumeonglog.global.error.code.WebClientErrorCode;

public class WebClientException extends GeneralException {
    public WebClientException(WebClientErrorCode code) {
        super(code);
    }
}
