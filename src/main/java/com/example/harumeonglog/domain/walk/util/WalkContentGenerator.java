package com.example.harumeonglog.domain.walk.util;

import com.example.harumeonglog.domain.walk.entity.Walk;
import com.example.harumeonglog.global.error.code.WalkErrorCode;
import com.example.harumeonglog.global.error.exception.WalkException;
import com.example.harumeonglog.global.util.WebClientUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
public class WalkContentGenerator {

    @Value("${openai.api.key}")
    private String apiKey;

    private final WebClient webClient;

    private static final int MAX_TOKEN = 150;
    private static final double TEMPERATURE = 0.7;
    private static final String MODEL = "gpt-4.1-nano";
    private static final String PROMPT_FORMAT = """
            다음 반려견과의 산책 데이터를 바탕으로 짧고 자연스러운 산책 일정 내용을 생성해주세요.

            산책 데이터:
            - 시작 시간: %s
            - 거리: %.1fkm
            - 시간: %d분
            - 시작 좌표(위도, 경도): %s
            - 경로 좌표(위도, 경도):
            %s

            요구사항:
            1. 1-2문장으로 간단하게 작성
            2. 거리와 시간을 자연스럽게 언급
            3. 친근하고 따뜻한 톤
            4. 한국어로 작성
            5. 이모지 0~2개 자연스럽게 넣기
            """;

    public WalkContentGenerator(WebClientUtil webClientUtil) {
        this.webClient = webClientUtil.getWebClient("https://api.openai.com/v1");
    }

    public String generateWalkSummary(Walk walk) {
        return generateWalkSummaryAsync(walk).block(Duration.ofSeconds(5));
    }

    public Mono<String> generateWalkSummaryAsync(Walk walk) {
        String prompt = String.format(
                PROMPT_FORMAT,
                walk.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                walk.getDistance(),
                walk.getTime(),
                String.format("(%.6f, %.6f)", walk.getStartLatitude(), walk.getStartLongitude()),
                walk.getTrackList().stream().map(track ->
                        track.getWalkPositionList().stream().map(walkPosition -> String.format("(%.6f, %.6f)", walkPosition.getLatitude(), walkPosition.getLongitude()))
                                .collect(Collectors.joining(", "))
                ).collect(Collectors.joining("\n"))
        );

        return callOpenAiApi(prompt);
    }

    private Mono<String> callOpenAiApi(String prompt) {
        Map<String, Object> requestBody = Map.of(
                "model", MODEL,
                "messages", List.of(
                        Map.of(
                                "role", "user",
                                "content", prompt
                        )
                ),
                "max_tokens", MAX_TOKEN,
                "temperature", TEMPERATURE
        );

        return webClient.post()
                .uri("/chat/completions")
                .header("Authorization", "Bearer " + apiKey)
                .header("Content-Type", "application/json")
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(Map.class)
                .mapNotNull(response -> {
                    List<Map<String, Object>> choices =
                            (List<Map<String, Object>>) response.get("choices");

                    if (choices != null && !choices.isEmpty()) {
                        Map<String, Object> message =
                                (Map<String, Object>) choices.get(0).get("message");
                        return (String) message.get("content");
                    }

                    return null;
                })
                .onErrorResume(e -> {
                    log.error("OpenAi api failed: {}", e.getMessage(), e);
                    return Mono.error(new WalkException(WalkErrorCode.FAIL_TO_CREATE_SUMMARY));
                });
    }
}
