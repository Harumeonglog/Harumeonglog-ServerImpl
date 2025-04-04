package com.example.harumeonglog.domain.common.auth.exception;

import com.example.harumeonglog.domain.common.domain.exception.GeneralException;

public class AuthException extends GeneralException {

    public AuthException(AuthErrorCode code) {
        super(code);
    }
}
