package com.example.harumeonglog.global.outbox.scheduler;

import com.example.harumeonglog.domain.event.entity.Event;
import com.example.harumeonglog.domain.event.entity.WalkEvent;
import com.example.harumeonglog.domain.event.repository.EventRepository;
import com.example.harumeonglog.domain.walk.entity.Walk;
import com.example.harumeonglog.domain.walk.service.WalkQueryService;
import com.example.harumeonglog.domain.walk.util.WalkContentGenerator;
import com.example.harumeonglog.global.error.code.EventErrorCode;
import com.example.harumeonglog.global.error.exception.EventException;
import com.example.harumeonglog.global.outbox.entity.OutBox;
import com.example.harumeonglog.global.outbox.entity.enums.EventType;
import com.example.harumeonglog.global.outbox.repository.OutBoxRepository;
import com.example.harumeonglog.global.util.S3Util;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class OutBoxScheduler {

    private final S3Util s3Util;

    private static final String PAYLOAD_SEPARATOR = "/";
    private static final int MAX_RETRY_COUNT = 3;
    private final OutBoxRepository outBoxRepository;
    private final EventRepository eventRepository;
    private final WalkQueryService walkQueryService;
    private final WalkContentGenerator walkContentGenerator;

    @Scheduled(fixedRate = 3600000)
    @Transactional
    public void processOutBoxAboutS3() {
        List<OutBox> events = outBoxRepository.findByEventTypeAndProcessedFalseAndRetryCountLessThan(
                EventType.S3, MAX_RETRY_COUNT);
        events.forEach(event -> {
            try {
                String imageKey = event.getPayload();
                if (s3Util.isObjectExists(imageKey)) {
                    log.info("S3 파일 확인 성공: key={}", imageKey);
                    s3Util.deleteFile(imageKey);
                } else {
                    log.warn("S3 파일이 존재하지 않음: key={}", imageKey);
                    event.increaseRetryCount();
                    if(event.getRetryCount() >= MAX_RETRY_COUNT) {
                        log.error("최대 재시도 횟수 초과. OutBox 제거: key={}", imageKey);
                    }
                }
            } catch (Exception e) {
                log.error("S3 파일 확인 실패 (key={}): {}", event.getPayload(), e.getMessage(), e);
                event.increaseRetryCount();
            }
        });
    }

    @Scheduled(fixedRate = 60000)
    @Transactional
    public void processOutBoxAboutWalkAiApi() {
        List<OutBox> list = outBoxRepository.findByEventTypeAndProcessedFalseAndRetryCountLessThan(
                EventType.WALK_AI_API,
                MAX_RETRY_COUNT
        );
        if (!list.isEmpty()) {
            log.info("{}개의 산책 내용 변경: {}", list.size(), LocalDateTime.now());
        }
        list.forEach(outbox -> {
            try {
                String[] ids = outbox.getPayload().split(PAYLOAD_SEPARATOR);
                Long walkId = Long.parseLong(ids[0]);
                Walk walk = walkQueryService.findById(walkId);
                String content = walkContentGenerator.generateWalkSummary(walk);
                if (content != null) {
                    Event event = eventRepository.findById(Long.parseLong(ids[1])).orElseThrow(() -> new EventException(EventErrorCode.NOT_FOUND));
                    if (event instanceof WalkEvent we) {
                        we.updateWalkDetails(content);
                    }
                    outbox.markProcessed();
                }
                else {
                    outbox.increaseRetryCount();
                }
            } catch (Exception e) {
                log.error("Fail Walk Content Generator: {}", e.getMessage(), e);
                outbox.increaseRetryCount();
            }
        });
    }
}
