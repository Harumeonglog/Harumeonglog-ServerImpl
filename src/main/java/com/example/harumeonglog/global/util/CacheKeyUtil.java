package com.example.harumeonglog.global.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 캐시 키 생성을 위한 유틸리티 클래스
 * 날짜 기반 캐시 키 계산 로직을 중앙화하여 일관성과 테스트 가능성 확보
 *
 * 스케줄러(3시)와 Cache Aside(4시)의 기준 시간이 의도적으로 분리되어 있음:
 * - 스케줄러가 3시에 새 캐시를 미리 적재
 * - Cache Aside는 4시에 키를 전환하여 캐시 미스 없이 새 데이터 사용
 * - 이 1시간 간격이 Cache Stampede를 방지함
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CacheKeyUtil {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final int SCHEDULER_REBUILD_HOUR = 3; // 스케줄러 캐시 적재 기준 시간
    private static final int CACHE_ASIDE_HOUR = 4; // @Cacheable 키 전환 기준 시간

    /**
     * @Cacheable용 캐시 키 날짜 생성 (새벽 4시 기준)
     *
     * - 00:00 ~ 03:59: 전전날 날짜 (이전 캐시를 계속 사용)
     * - 04:00 ~ 23:59: 어제 날짜 (스케줄러가 3시에 미리 적재한 캐시 사용)
     *
     * @return yyyy-MM-dd 형식의 날짜 문자열
     */
    public static String getYesterdayPostsCacheDate() {
        LocalDateTime now = LocalDateTime.now();
        LocalDate today = now.toLocalDate();
        int currentHour = now.getHour();

        LocalDate targetDate = (currentHour < CACHE_ASIDE_HOUR)
                ? today.minusDays(2)
                : today.minusDays(1);

        return targetDate.format(DATE_FORMATTER);
    }

    /**
     * 스케줄러용 어제 날짜 반환 (새벽 3시 기준)
     * DB 쿼리에 사용하기 위한 메서드
     *
     * @return 새벽 3시 기준 어제 날짜 (LocalDate)
     */
    public static LocalDate getYesterdayDate() {
        LocalDateTime now = LocalDateTime.now();
        LocalDate today = now.toLocalDate();
        int currentHour = now.getHour();

        return (currentHour < SCHEDULER_REBUILD_HOUR)
                ? today.minusDays(2)
                : today.minusDays(1);
    }

    /**
     * 스케줄러용 캐시 키 생성 (새벽 3시 기준)
     *
     * 스케줄러가 3시에 이 키로 캐시를 적재하면,
     * 4시에 @Cacheable이 같은 키로 전환되어 캐시 히트가 보장됨
     *
     * @return "posts:yesterday:yyyy-MM-dd" 형식의 캐시 키
     */
    public static String getSchedulerCacheKey() {
        LocalDateTime now = LocalDateTime.now();
        LocalDate today = now.toLocalDate();
        int currentHour = now.getHour();

        LocalDate targetDate = (currentHour < SCHEDULER_REBUILD_HOUR)
                ? today.minusDays(2)
                : today.minusDays(1);

        return "posts:yesterday:" + targetDate.format(DATE_FORMATTER);
    }

    /**
     * @Cacheable용 캐시 키 생성 (새벽 4시 기준)
     *
     * @return "posts:yesterday:yyyy-MM-dd" 형식의 캐시 키
     */
    public static String getYesterdayPostsCacheKey() {
        return "posts:yesterday:" + getYesterdayPostsCacheDate();
    }
}
