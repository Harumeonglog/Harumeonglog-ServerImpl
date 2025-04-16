package com.example.harumeonglog.domain.pet.controller.specification;


import com.example.harumeonglog.domain.pet.dto.request.PetImageRequest;
import com.example.harumeonglog.domain.pet.dto.response.PetImageResponse;
import com.example.harumeonglog.global.common.response.CustomResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "PetImage", description = "Pet 이미지 관련 API")
@RequestMapping("/pet-images")
public interface PetImageControllerSpecification {

    @Operation(summary = "펫 이미지 등록 API by 백종우", description = "펫 이미지 여러 개를 등록합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "COMMON201", description = "등록 성공")
    })
    @PostMapping("/{petId}")
    ResponseEntity<CustomResponse<PetImageResponse.AddImagesResponse>> addImages(
            @PathVariable Long petId, @RequestBody PetImageRequest.AddImagesRequest request
    );

    @Operation(summary = "펫 이미지 목록 조회 API by 백종우", description = "펫 이미지 목록을 커서 기반으로 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "COMMON200", description = "조회 성공")
    })
    @GetMapping("/{petId}")
    CustomResponse<PetImageResponse.GetImagesResponse> getImages(
            @PathVariable Long petId,
            @RequestParam(required = false) Long cursor,
            @RequestParam(defaultValue = "10") int size
    );

    @Operation(summary = "최근 등록된 이미지 조회 API by 백종우", description = "최근에 등록된 펫 이미지 목록을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "COMMON200", description = "조회 성공")
    })
    @GetMapping("/recent")
    CustomResponse<PetImageResponse.RecentImagesResponse> recentImages();

    @Operation(summary = "단일 이미지 조회 API by 백종우", description = "특정 이미지 ID에 해당하는 펫 이미지를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "COMMON200", description = "조회 성공")
    })
    @GetMapping("/image/{imageId}")
    CustomResponse<PetImageResponse.GetImageResponse> getImage(@PathVariable Long imageId);

    @Operation(summary = "단일 이미지 삭제 API by 백종우", description = "특정 이미지 ID의 펫 이미지를 삭제합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "COMMON200", description = "삭제 성공")
    })
    @DeleteMapping("/image/{imageId}")
    CustomResponse<String> deleteImage(@PathVariable Long imageId);

    @Operation(summary = "여러 이미지 삭제 API by 백종우", description = "펫 ID와 이미지 ID 목록을 통해 여러 이미지를 삭제합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "COMMON200", description = "삭제 성공")
    })
    @DeleteMapping("/{petId}")
    CustomResponse<String> deleteImages(
            @PathVariable Long petId, @RequestBody PetImageRequest.DeleteImagesRequest request
    );
}