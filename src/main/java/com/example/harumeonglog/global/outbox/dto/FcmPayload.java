package com.example.harumeonglog.global.outbox.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FcmPayload {
    private Long receiverId;
    private String title;
    private String body;
    private String noticeType;
}
