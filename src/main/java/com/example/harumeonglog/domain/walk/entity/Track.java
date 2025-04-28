package com.example.harumeonglog.domain.walk.entity;

import com.example.harumeonglog.global.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "track")
public class Track extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "track_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "walk_id", nullable = false)
    private Walk walk;

    @OneToMany(mappedBy = "track", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WalkPosition> walkPositionList = new ArrayList<>();
}
