package com.example.harumeonglog.domain.walk.dto.event;

import lombok.Builder;
import lombok.Getter;

public class WalkApplicationEvents {

    private WalkApplicationEvents() {}

    @Getter
    @Builder
    public static class WalkContentEvent {
        private Long walkId;
        private Long eventId;

        public static WalkContentEvent from(Long walkId, Long eventId) {
            return WalkContentEvent.builder()
                    .walkId(walkId)
                    .eventId(eventId)
                    .build();
        }
    }
}
