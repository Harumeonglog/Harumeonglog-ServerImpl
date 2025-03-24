package com.example.harumeonglog.domain.walk.domain;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class Walk {

    private Long id;

    private String title;

    private Double distance;

    private Integer time;

    private Double startLatitude;

    private Double startLongitude;

    private Long walkLikeNum;

    private Boolean isShared;

    private LocalDateTime deletedAt;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
