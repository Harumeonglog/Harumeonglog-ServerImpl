package com.example.harumeonglog.domain.pet.converter;

import com.example.harumeonglog.domain.pet.dto.response.PetImageResponse;
import com.example.harumeonglog.domain.pet.entity.Pet;
import com.example.harumeonglog.domain.pet.entity.PetImage;

import java.util.List;


public class PetImageConverter {
    public static PetImage toPetImage(Pet pet, String imageKey) {
        return PetImage.builder()
                .imageKey(imageKey)
                .pet(pet)
                .build();
    }

    public static PetImageResponse.AddImagesResponse toAddImagesResponse(List<Long> petImages) {
        return PetImageResponse.AddImagesResponse.builder()
                .imageIds(petImages)
                .build();
    }
}
