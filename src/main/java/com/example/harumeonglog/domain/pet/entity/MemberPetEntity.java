package com.example.harumeonglog.domain.pet.entity;

import com.example.harumeonglog.domain.member.domain.enums.MemberPetRole;
import com.example.harumeonglog.domain.member.entity.MemberEntity;
import com.example.harumeonglog.domain.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "member_pet")
public class MemberPetEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_pet_id")
    private Long id;

    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    private MemberPetRole role;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pet_id", nullable = false)
    private PetEntity petEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private MemberEntity memberEntity;
}
