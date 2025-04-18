package com.example.harumeonglog.global.error.exception;

import com.example.harumeonglog.global.error.code.TokenErrorCode;

public class TokenException extends GeneralException {
    public TokenException(TokenErrorCode code) {
        super(code);
    }
}
