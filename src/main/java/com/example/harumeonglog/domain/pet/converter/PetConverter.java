package com.example.harumeonglog.domain.pet.converter;

import com.example.harumeonglog.domain.pet.dto.request.PetRequest;
import com.example.harumeonglog.domain.pet.dto.response.PetResponse;
import com.example.harumeonglog.domain.pet.entity.Pet;

public class PetConverter {

    public static Pet toPet(PetRequest.AddPetRequest request){
        return Pet.builder()
                .name(request.getName())
                .size(request.getSize())
                .type(request.getType())
                .gender(request.getGender())
                .birth(request.getBirth())
                .build();
    }

    public static PetResponse.AddPetResponse toAddPetResponse(Pet pet){
        return PetResponse.AddPetResponse.builder()
                .petId(pet.getId())
                .build();
    }

    public static PetResponse.ChangePetInfoResponse toChangePetInfoResponse(Pet pet){
        return PetResponse.ChangePetInfoResponse.builder()
                .petId(pet.getId())
                .name(pet.getName())
                .size(pet.getSize())
                .type(pet.getType())
                .gender(pet.getGender())
                .birth(pet.getBirth())
                .mainImage(pet.getMainImage())
                .build();
    }
}
