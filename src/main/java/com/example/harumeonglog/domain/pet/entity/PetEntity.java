package com.example.harumeonglog.domain.pet.entity;

import com.example.harumeonglog.domain.common.entity.BaseEntity;
import com.example.harumeonglog.domain.pet.domain.enums.Gender;
import com.example.harumeonglog.domain.pet.domain.enums.PetSize;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "pet")
public class PetEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pet_id")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "size", nullable = false)
    @Enumerated(EnumType.STRING)
    private PetSize size;

    @Column(name = "type", nullable = false)
    private String type;

    @Column(name = "gender", nullable = false)
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(name = "birth", nullable = false)
    private LocalDate birth;

    @Column(name = "main_image", nullable = false)
    private String mainImage;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;
}
