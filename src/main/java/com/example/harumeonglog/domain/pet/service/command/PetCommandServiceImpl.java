package com.example.harumeonglog.domain.pet.service.command;

import com.example.harumeonglog.domain.member.entity.Member;
import com.example.harumeonglog.domain.member.entity.enums.MemberPetRole;
import com.example.harumeonglog.domain.member.infrastructure.MemberRepository;
import com.example.harumeonglog.domain.pet.converter.MemberPetConverter;
import com.example.harumeonglog.domain.pet.converter.PetConverter;
import com.example.harumeonglog.domain.pet.dto.request.PetRequest;
import com.example.harumeonglog.domain.pet.dto.response.PetResponse;
import com.example.harumeonglog.domain.pet.entity.MemberPet;
import com.example.harumeonglog.domain.pet.entity.Pet;
import com.example.harumeonglog.domain.pet.repository.MemberPetRepository;
import com.example.harumeonglog.domain.pet.repository.PetRepository;
import com.example.harumeonglog.global.error.code.MemberErrorCode;
import com.example.harumeonglog.global.error.code.PetErrorCode;
import com.example.harumeonglog.global.error.code.S3ErrorCode;
import com.example.harumeonglog.global.error.exception.MemberException;
import com.example.harumeonglog.global.error.exception.PetException;
import com.example.harumeonglog.global.error.exception.S3Exception;
import com.example.harumeonglog.global.util.S3Util;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class PetCommandServiceImpl implements PetCommandService {
    private final PetRepository petRepository;
    private final MemberPetRepository memberPetRepository;
    private final MemberRepository memberRepository;
    private final S3Util s3Util;


    @Override
    public PetResponse.AddPetResponse addPet(PetRequest.AddPetRequest request, MultipartFile mainImage, Member member) {
        // 유효성 검사
        if (mainImage == null || mainImage.isEmpty()) {
            throw new PetException(PetErrorCode.IMAGE_NOT_FOUND);
        }
        try {
            // Pet 엔터티 생성
            Pet pet = PetConverter.toPet(request);
            Pet savedPet = petRepository.save(pet);

            // S3 키 생성
            String mainImageKey = String.format("pet/%d/main/%s/%s",
                    pet.getId(), UUID.randomUUID(), mainImage.getOriginalFilename());

            // S3에 이미지 업로드
            s3Util.uploadFile(mainImage, mainImageKey);

            // pet에 이미지 저장
            savedPet.setMainImage(mainImageKey);

            // memberPet 생성
            MemberPet memberPet = MemberPetConverter.toMemberPet(member, pet, MemberPetRole.OWNER);
            memberPetRepository.save(memberPet);

            // member의 currentPetId 지정
            member.setCurrentPetId(pet.getId());

            return PetConverter.toAddPetResponse(savedPet);
        } catch (IOException e) {
            throw new S3Exception(S3ErrorCode.UPLOAD_FAILED);
        } catch (S3Exception e) {
            throw new S3Exception(S3ErrorCode.SERVICE_UNAVAILABLE);
        }
    }

    @Override
    public PetResponse.ChangePetInfoResponse changePetInfo(Long petId, PetRequest.ChangePetInfoRequest request, MultipartFile mainImage, Member member) {
        // Pet 조회
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new PetException(PetErrorCode.NOT_FOUND));

        // 해당 MEMBER, PET이 없을 경우 에러처리
        MemberPet memberPet = memberPetRepository.findByMemberAndPet(member, pet)
                .orElseThrow(() -> new PetException(PetErrorCode.NOT_IN_GROUP));

        // OWNER 권한이 아닐경우 에러처리
        if(!memberPet.getRole().equals(MemberPetRole.OWNER)) {
            throw new PetException(PetErrorCode.NOT_ALLOWED_ROLE);
        }

        try {
            String newMainImageKey = pet.getMainImage(); // 기존 키 유지

            // mainImage가 제공된 경우 기존 이미지 삭제 및 새 이미지 업로드
            if (mainImage != null && !mainImage.isEmpty()) {
                // 기존 이미지 삭제 (기존 키가 null이 아닌 경우)
                if (pet.getMainImage() != null) {
                    s3Util.deleteFile(pet.getMainImage());
                }

                // 새 S3 키 생성
                newMainImageKey = String.format("pet/%d/main/%s/%s",
                        pet.getId(), UUID.randomUUID(), mainImage.getOriginalFilename());

                // S3에 새 이미지 업로드
                s3Util.uploadFile(mainImage, newMainImageKey);
            }

            // Pet 정보 업데이트
            pet.update(
                    request.getName(),
                    request.getSize(),
                    request.getType(),
                    request.getGender(),
                    request.getBirth(),
                    newMainImageKey
            );


            String image = s3Util.getFilePresignedUrl(newMainImageKey, 60);

            // 응답 DTO 반환
            return PetConverter.toChangePetInfoResponse(pet, image);
        } catch (IOException e) {
            throw new S3Exception(S3ErrorCode.UPLOAD_FAILED);
        } catch (S3Exception e) {
            throw new S3Exception(S3ErrorCode.SERVICE_UNAVAILABLE);
        }
    }


    @Override
    public void changeCurrentPet(PetRequest.ChangeCurrentPetRequest request, Member member) {
        Pet pet = petRepository.findById(request.getPetId()).orElseThrow(
                () -> new PetException(PetErrorCode.NOT_FOUND));

        // 해당 MEMBER, PET이 없을 경우 에러처리
        memberPetRepository.findByMemberAndPet(member, pet)
                .orElseThrow(() -> new PetException(PetErrorCode.NOT_IN_GROUP));

        member.setCurrentPetId(pet.getId());
    }

    @Override
    public void deletePet(Long petId, Member member) {
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new PetException(PetErrorCode.NOT_FOUND));

        // 권한 확인 (OWNER만 삭제 가능)
        MemberPet memberPet = memberPetRepository.findByMemberAndPet(member, pet)
                .orElseThrow(() -> new PetException(PetErrorCode.NOT_IN_GROUP));
        if (!memberPet.getRole().equals(MemberPetRole.OWNER)) {
            throw new PetException(PetErrorCode.NOT_ALLOWED_ROLE);
        }

        // pet soft delete
        pet.softDelete();

        // memberPet hard delete
        memberPetRepository.deleteByPet(pet);
    }

    @Override
    public void invite(Long petId, PetRequest.InviteListRequest request, Member member) {
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new PetException(PetErrorCode.NOT_FOUND));

        // 권한 확인 (OWNER만 삭제 가능)
        MemberPet memberPet = memberPetRepository.findByMemberAndPet(member, pet)
                .orElseThrow(() -> new PetException(PetErrorCode.NOT_IN_GROUP));
        if (!memberPet.getRole().equals(MemberPetRole.OWNER)) {
            throw new PetException(PetErrorCode.NOT_ALLOWED_ROLE);
        }

        // 초대 처리
        request.getRequests().stream()
                .filter(invite -> {
                    boolean exists = memberPetRepository.findByMemberAndPet(
                            memberRepository.findById(invite.getMemberId())
                                    .orElseThrow(() -> new MemberException(MemberErrorCode.NOT_FOUND)),
                            pet
                    ).isPresent();
                    if (exists) {
                        // 이미 초대된 상태인 경우 에러처리
                        throw new PetException(PetErrorCode.ALREADY_INVITED);
                    }
                    return true;
                })
                .map(invite -> {
                    MemberPetRole role;
                    try{
                        role = MemberPetRole.valueOf(invite.getRole());
                    }catch (IllegalArgumentException e){
                        throw new PetException(PetErrorCode.INVALID_ROLE);
                    }
                    return MemberPetConverter.toMemberPet(member, pet, role);
                })
                .forEach(memberPetRepository::save);
    }

}
