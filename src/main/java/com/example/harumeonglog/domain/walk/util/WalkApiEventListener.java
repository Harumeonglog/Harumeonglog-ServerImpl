package com.example.harumeonglog.domain.walk.util;

import com.example.harumeonglog.domain.event.entity.Event;
import com.example.harumeonglog.domain.event.repository.EventRepository;
import com.example.harumeonglog.domain.walk.dto.event.WalkApplicationEvents;
import com.example.harumeonglog.domain.walk.entity.Walk;
import com.example.harumeonglog.domain.walk.repository.WalkRepository;
import com.example.harumeonglog.global.error.code.EventErrorCode;
import com.example.harumeonglog.global.error.code.WalkErrorCode;
import com.example.harumeonglog.global.error.exception.EventException;
import com.example.harumeonglog.global.error.exception.WalkException;
import io.netty.handler.timeout.TimeoutException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.web.reactive.function.client.WebClientException;
import reactor.util.retry.Retry;

import java.time.Duration;

@Slf4j
@Component
@RequiredArgsConstructor
public class WalkApiEventListener {

    private final WalkContentGenerator walkContentGenerator;

    private final WalkRepository walkRepository;
    private final EventRepository eventRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void walkAiApiEvent(WalkApplicationEvents.WalkContentEvent walkContentEvent) {
        Walk walk = walkRepository.findById(walkContentEvent.getWalkId()).orElseThrow(() -> new WalkException(WalkErrorCode.NOT_FOUND));
        walkContentGenerator.generateWalkSummaryAsync(walk)
                .retryWhen(
                        Retry.backoff(3, Duration.ofSeconds(1))
                                .filter(e -> e instanceof WebClientException || e instanceof TimeoutException)
                )
                .subscribe(content -> {
                    Event event = eventRepository.findById(walkContentEvent.getEventId()).orElseThrow(() -> new EventException(EventErrorCode.NOT_FOUND));
                    if (event instanceof com.example.harumeonglog.domain.event.entity.WalkEvent we) {
                        we.updateWalkDetails(content);
                        eventRepository.save(event);
                    }
                });
    }
}
