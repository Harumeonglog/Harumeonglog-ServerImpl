package com.example.harumeonglog.domain.pet.service.command;

import com.example.harumeonglog.domain.member.entity.Member;
import com.example.harumeonglog.domain.member.entity.enums.MemberPetRole;
import com.example.harumeonglog.domain.pet.converter.PetImageConverter;
import com.example.harumeonglog.domain.pet.dto.request.PetImageRequest;
import com.example.harumeonglog.domain.pet.dto.response.PetImageResponse;
import com.example.harumeonglog.domain.pet.entity.MemberPet;
import com.example.harumeonglog.domain.pet.entity.Pet;
import com.example.harumeonglog.domain.pet.entity.PetImage;
import com.example.harumeonglog.domain.pet.repository.MemberPetRepository;
import com.example.harumeonglog.domain.pet.repository.PetImageRepository;
import com.example.harumeonglog.domain.pet.repository.PetRepository;
import com.example.harumeonglog.global.error.code.PetErrorCode;
import com.example.harumeonglog.global.error.code.S3ErrorCode;
import com.example.harumeonglog.global.error.exception.PetException;
import com.example.harumeonglog.global.error.exception.S3Exception;
import com.example.harumeonglog.global.util.S3Util;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class PetImageCommandServiceImpl implements PetImageCommandService {
    private final PetImageRepository petImageRepository;
    private final PetRepository petRepository;
    private final MemberPetRepository memberPetRepository;
    private final S3Util s3Util;

    // Pet 엔티티 조회
    private Pet findPetById(Long petId) {
        return petRepository.findById(petId)
                .orElseThrow(() -> new PetException(PetErrorCode.NOT_FOUND));
    }

    // 사용자 OWNER 검증
    private void validateOwnerAccess(Member member, Pet pet) {
        MemberPet memberPet = memberPetRepository.findByMemberAndPet(member, pet)
                .orElseThrow(() -> new PetException(PetErrorCode.NOT_IN_GROUP));

        if (!memberPet.getRole().equals(MemberPetRole.OWNER)) {
            throw new PetException(PetErrorCode.NOT_ALLOWED_ROLE);
        }
    }

    @Override
    public PetImageResponse.AddImagesResponse addImages(Long petId, Member member, List<MultipartFile> images) {

        Pet pet = findPetById(petId);
        validateOwnerAccess(member, pet);

        // 이미지 업로드 및 저장
        List<Long> imageIds = images.stream()
                .map(image -> {
                    try {
                        String imageKey = String.format("pet/%d/gallery/%s/%s",
                                petId, UUID.randomUUID(), image.getOriginalFilename());
                        s3Util.uploadFile(image, imageKey);
                        PetImage petImage = PetImageConverter.toPetImage(pet, imageKey);
                        PetImage savedImage = petImageRepository.save(petImage);
                        return savedImage.getId();
                    } catch (IOException e) {
                        throw new S3Exception(S3ErrorCode.UPLOAD_FAILED);
                    } catch (S3Exception e) {
                        throw new S3Exception(S3ErrorCode.SERVICE_UNAVAILABLE);
                    }
                })
                .toList();

        return PetImageConverter.toAddImagesResponse(imageIds);
    }

    @Override
    public void deleteImage(Long imageId) {

    }

    @Override
    public void deleteImages(Long petId, PetImageRequest.DeleteImagesRequest request) {

    }
}
