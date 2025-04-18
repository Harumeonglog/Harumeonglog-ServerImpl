package com.example.harumeonglog.domain.pet.service.query;

import com.example.harumeonglog.domain.pet.dto.response.PetImageResponse;

public interface PetImageQueryService {
    PetImageResponse.GetImagesResponse getImages(Long petId, Long cursor, int size);
    PetImageResponse.RecentImagesResponse recentImages();
    PetImageResponse.GetImageResponse getImage(Long imageId);
}
