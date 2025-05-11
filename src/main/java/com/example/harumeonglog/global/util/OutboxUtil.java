package com.example.harumeonglog.global.util;

import com.example.harumeonglog.global.error.code.OutboxErrorCode;
import com.example.harumeonglog.global.error.exception.OutboxException;
import com.example.harumeonglog.global.outbox.entity.OutBox;
import com.example.harumeonglog.global.outbox.entity.enums.EventType;
import com.example.harumeonglog.global.outbox.repository.OutBoxRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OutboxUtil {
    private final OutBoxRepository outBoxRepository;

    public void changeOutboxStatus(String payload, EventType eventType) {
        if(payload != null) {
            OutBox outBox = outBoxRepository.findByPayloadAndEventType(payload, eventType)
                    .orElseThrow(() -> new OutboxException(OutboxErrorCode.NOT_FOUND));

            outBox.markProcessed();
        }
    }
}
