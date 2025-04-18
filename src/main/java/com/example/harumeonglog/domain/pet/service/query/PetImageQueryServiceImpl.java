package com.example.harumeonglog.domain.pet.service.query;

import com.example.harumeonglog.domain.pet.dto.response.PetImageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PetImageQueryServiceImpl implements PetImageQueryService {
    @Override
    public PetImageResponse.GetImagesResponse getImages(Long petId, Long cursor, int size) {
        return null;
    }

    @Override
    public PetImageResponse.RecentImagesResponse recentImages() {
        return null;
    }

    @Override
    public PetImageResponse.GetImageResponse getImage(Long imageId) {
        return null;
    }
}
