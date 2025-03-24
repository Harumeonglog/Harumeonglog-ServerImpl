package com.example.harumeonglog.domain.pet.domain;




import com.example.harumeonglog.domain.pet.domain.enums.Gender;
import com.example.harumeonglog.domain.pet.domain.enums.PetSize;
import lombok.Builder;
import lombok.Getter;


import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Builder
public class Pet {

    private Long id;

    private String name;

    private PetSize size;

    private String type;

    private Gender gender;

    private LocalDate birth;

    private String mainImage;

    private LocalDateTime deletedAt;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
