package com.example.harumeonglog.global.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * CacheKeyUtil 테스트
 *
 * 핵심 테스트 케이스:
 * 1. @Cacheable용: 새벽 4시 기준으로 키 전환
 * 2. 스케줄러용: 새벽 3시 기준으로 키 생성
 * 3. 3시~4시 사이에 스케줄러 키와 4시 이후 @Cacheable 키가 일치하는지 검증
 */
@DisplayName("CacheKeyUtil 테스트")
class CacheKeyUtilTest {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @DisplayName("@Cacheable: 새벽 4시 이전(00:00~03:59)에는 전전날 날짜를 반환한다")
    @ParameterizedTest(name = "{0}시 → 전전날 날짜")
    @MethodSource("provideHoursBeforeFourAM")
    void shouldReturnDayBeforeYesterdayWhenBeforeFourAM(int hour) {
        // when
        String result = CacheKeyUtil.getYesterdayPostsCacheDate();

        // then
        LocalDateTime now = LocalDateTime.now();
        if (now.getHour() < 4) {
            String expected = now.toLocalDate().minusDays(2).format(DATE_FORMATTER);
            assertThat(result).isEqualTo(expected);
        }
    }

    @DisplayName("@Cacheable: 새벽 4시 이후(04:00~23:59)에는 어제 날짜를 반환한다")
    @ParameterizedTest(name = "{0}시 → 어제 날짜")
    @MethodSource("provideHoursAfterFourAM")
    void shouldReturnYesterdayWhenAfterFourAM(int hour) {
        // when
        String result = CacheKeyUtil.getYesterdayPostsCacheDate();

        // then
        LocalDateTime now = LocalDateTime.now();
        if (now.getHour() >= 4) {
            String expected = now.toLocalDate().minusDays(1).format(DATE_FORMATTER);
            assertThat(result).isEqualTo(expected);
        }
    }

    @Test
    @DisplayName("@Cacheable용 캐시 키는 'posts:yesterday:yyyy-MM-dd' 형식이어야 한다")
    void shouldReturnCorrectCacheKeyFormat() {
        // when
        String cacheKey = CacheKeyUtil.getYesterdayPostsCacheKey();

        // then
        assertThat(cacheKey).startsWith("posts:yesterday:");
        assertThat(cacheKey).matches("posts:yesterday:\\d{4}-\\d{2}-\\d{2}");
    }

    @Test
    @DisplayName("스케줄러용 캐시 키는 'posts:yesterday:yyyy-MM-dd' 형식이어야 한다")
    void shouldReturnCorrectSchedulerCacheKeyFormat() {
        // when
        String cacheKey = CacheKeyUtil.getSchedulerCacheKey();

        // then
        assertThat(cacheKey).startsWith("posts:yesterday:");
        assertThat(cacheKey).matches("posts:yesterday:\\d{4}-\\d{2}-\\d{2}");
    }

    @Test
    @DisplayName("@Cacheable용 캐시 키는 날짜 부분을 포함해야 한다")
    void shouldContainDateInCacheKey() {
        // when
        String cacheKey = CacheKeyUtil.getYesterdayPostsCacheKey();
        String dateOnly = CacheKeyUtil.getYesterdayPostsCacheDate();

        // then
        assertThat(cacheKey).endsWith(dateOnly);
        assertThat(cacheKey).isEqualTo("posts:yesterday:" + dateOnly);
    }

    @Test
    @DisplayName("동일 시점에서 여러 번 호출해도 같은 키를 반환한다")
    void shouldReturnConsistentKeyOnMultipleCalls() {
        // when
        String key1 = CacheKeyUtil.getYesterdayPostsCacheKey();
        String key2 = CacheKeyUtil.getYesterdayPostsCacheKey();
        String key3 = CacheKeyUtil.getYesterdayPostsCacheKey();

        // then
        assertThat(key1)
                .isEqualTo(key2)
                .isEqualTo(key3);
    }

    @Test
    @DisplayName("날짜 문자열은 yyyy-MM-dd 형식이어야 한다")
    void shouldReturnDateInCorrectFormat() {
        // when
        String date = CacheKeyUtil.getYesterdayPostsCacheDate();

        // then
        assertThat(date).matches("\\d{4}-\\d{2}-\\d{2}");
        assertThat(LocalDate.parse(date, DATE_FORMATTER)).isNotNull();
    }

    @Test
    @DisplayName("현재 시간 기준으로 @Cacheable용 캐시 날짜를 올바르게 계산한다 (4시 기준)")
    void shouldCalculateCorrectCacheDateBasedOnCurrentTime() {
        // given
        LocalDateTime now = LocalDateTime.now();
        LocalDate today = now.toLocalDate();
        int currentHour = now.getHour();

        // expected
        LocalDate expectedDate = (currentHour < 4)
                ? today.minusDays(2)
                : today.minusDays(1);
        String expectedDateString = expectedDate.format(DATE_FORMATTER);

        // when
        String actualDate = CacheKeyUtil.getYesterdayPostsCacheDate();

        // then
        assertThat(actualDate).isEqualTo(expectedDateString);
    }

    @Test
    @DisplayName("현재 시간 기준으로 스케줄러용 어제 날짜를 올바르게 계산한다 (3시 기준)")
    void shouldCalculateCorrectYesterdayDateForScheduler() {
        // given
        LocalDateTime now = LocalDateTime.now();
        LocalDate today = now.toLocalDate();
        int currentHour = now.getHour();

        // expected
        LocalDate expectedDate = (currentHour < 3)
                ? today.minusDays(2)
                : today.minusDays(1);

        // when
        LocalDate actualDate = CacheKeyUtil.getYesterdayDate();

        // then
        assertThat(actualDate).isEqualTo(expectedDate);
    }

    @Test
    @DisplayName("3시 이후에 스케줄러 키와 @Cacheable 키가 일치해야 한다 (4시 이후)")
    void shouldMatchSchedulerKeyAndCacheAsideKeyAfterFourAM() {
        // when
        String schedulerKey = CacheKeyUtil.getSchedulerCacheKey();
        String cacheAsideKey = CacheKeyUtil.getYesterdayPostsCacheKey();

        // then
        LocalDateTime now = LocalDateTime.now();
        if (now.getHour() >= 4) {
            assertThat(schedulerKey).isEqualTo(cacheAsideKey);
        }
    }

    @Test
    @DisplayName("새벽 4시 경계값에서 올바르게 동작한다")
    void shouldHandleBoundaryAtFourAM() {
        // when
        String cacheKey = CacheKeyUtil.getYesterdayPostsCacheKey();

        // then
        assertThat(cacheKey).isNotNull();
        assertThat(cacheKey).isNotEmpty();

        LocalDateTime now = LocalDateTime.now();
        if (now.getHour() == 4) {
            String expectedDate = now.toLocalDate().minusDays(1).format(DATE_FORMATTER);
            assertThat(cacheKey).isEqualTo("posts:yesterday:" + expectedDate);
        }
    }

    private static Stream<Arguments> provideHoursBeforeFourAM() {
        return Stream.of(
                Arguments.of(0),
                Arguments.of(1),
                Arguments.of(2),
                Arguments.of(3)
        );
    }

    private static Stream<Arguments> provideHoursAfterFourAM() {
        return Stream.of(
                Arguments.of(4),
                Arguments.of(5),
                Arguments.of(12),
                Arguments.of(18),
                Arguments.of(23)
        );
    }
}
