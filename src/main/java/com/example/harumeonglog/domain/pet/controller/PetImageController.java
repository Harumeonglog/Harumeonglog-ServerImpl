package com.example.harumeonglog.domain.pet.controller;

import com.example.harumeonglog.domain.member.entity.Member;
import com.example.harumeonglog.domain.pet.controller.specification.PetImageControllerSpecification;
import com.example.harumeonglog.domain.pet.dto.response.PetImageResponse;
import com.example.harumeonglog.domain.pet.service.query.PetImageQueryService;
import com.example.harumeonglog.global.common.response.CustomResponse;
import com.example.harumeonglog.domain.pet.dto.request.PetImageRequest.AddImagesRequest;
import com.example.harumeonglog.domain.pet.dto.request.PetImageRequest.DeleteImagesRequest;
import com.example.harumeonglog.domain.pet.service.command.PetImageCommandService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/pet-images")
@Tag(name = "PetImage", description = "Pet 이미지 관련 API")
public class PetImageController implements PetImageControllerSpecification {

    private final PetImageCommandService petImageCommandService;
    private final PetImageQueryService petImageQueryService;

    @PostMapping(value = "/{petId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CustomResponse<PetImageResponse.AddImagesResponse>> addImages(
            @PathVariable Long petId,
            @RequestPart List<MultipartFile> images,
            @AuthenticationPrincipal Member member) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(CustomResponse.created(petImageCommandService.addImages(petId, member, images)));
    }

    @GetMapping("/{petId}")
    public CustomResponse<PetImageResponse.GetImagesResponse> getImages(
            @PathVariable Long petId,
            @RequestParam(required = false) Long cursor,
            @RequestParam(defaultValue = "10") int size,
            @AuthenticationPrincipal Member member) {
        return CustomResponse.ok(petImageQueryService.getImages(petId, member, cursor, size));
    }

//    @GetMapping("/recent")
//    public CustomResponse<PetImageResponse.RecentImagesResponse> recentImages() {
//        return CustomResponse.ok(petImageQueryService.recentImages());
//    }

    @GetMapping("/image/{imageId}")
    public CustomResponse<PetImageResponse.GetImageResponse> getImage(
            @PathVariable Long imageId,
            @AuthenticationPrincipal Member member) {
        return CustomResponse.ok(petImageQueryService.getImage(imageId, member));
    }

    @DeleteMapping("/image/{imageId}")
    public CustomResponse<String> deleteImage(
            @PathVariable Long imageId,
            @AuthenticationPrincipal Member member) {
        petImageCommandService.deleteImage(imageId, member);
        return CustomResponse.ok("이미지 삭제 완료");
    }

    @DeleteMapping("/{petId}")
    public CustomResponse<String> deleteImages(
            @PathVariable Long petId,
            @RequestBody DeleteImagesRequest request,
            @AuthenticationPrincipal Member member) {
        petImageCommandService.deleteImages(petId, request, member);
        return CustomResponse.ok("이미지 삭제 완료");
    }
}