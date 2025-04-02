package com.example.harumeonglog.domain.member.exception;

import com.example.harumeonglog.domain.common.domain.exception.GeneralException;

public class MemberException extends GeneralException {
    public MemberException(MemberErrorCode code) {
        super(code);
    }
}
