package com.example.harumeonglog.domain.member.entity;

import com.example.harumeonglog.domain.common.entity.BaseEntity;
import com.example.harumeonglog.domain.member.domain.enums.NoticeType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "notice")
public class NoticeEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notice_id")
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "content")
    private String content;

    @Column(name = "notice_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private NoticeType noticeType;

    @Column(name = "target_id")
    private Long targetId;
}
