package com.example.harumeonglog.global.error.exception;

import com.example.harumeonglog.global.error.code.BaseErrorCode;

public class OutboxException extends GeneralException {
    public OutboxException(BaseErrorCode errorCode) {
        super(errorCode);
    }
}
