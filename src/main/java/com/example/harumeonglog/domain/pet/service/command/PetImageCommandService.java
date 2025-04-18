package com.example.harumeonglog.domain.pet.service.command;

import com.example.harumeonglog.domain.member.entity.Member;
import com.example.harumeonglog.domain.pet.dto.request.PetImageRequest;
import com.example.harumeonglog.domain.pet.dto.response.PetImageResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PetImageCommandService {
    PetImageResponse.AddImagesResponse addImages(Long petId, Member member, List<MultipartFile> images);
    void deleteImage(Long imageId, Member member);
    void deleteImages(Long petId, PetImageRequest.DeleteImagesRequest request, Member member);
}
