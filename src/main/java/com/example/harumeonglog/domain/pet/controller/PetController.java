package com.example.harumeonglog.domain.pet.controller;


import com.example.harumeonglog.domain.member.entity.Member;
import com.example.harumeonglog.domain.pet.dto.request.PetRequest;
import com.example.harumeonglog.domain.pet.dto.request.PetRequest.AddPetRequest;
import com.example.harumeonglog.domain.pet.dto.request.PetRequest.ChangePetInfoRequest;
import com.example.harumeonglog.domain.pet.dto.response.PetResponse;
import com.example.harumeonglog.domain.pet.dto.response.PetResponse.AddPetResponse;
import com.example.harumeonglog.domain.pet.dto.response.PetResponse.ChangePetInfoResponse;
import com.example.harumeonglog.domain.pet.service.command.PetCommandService;
import com.example.harumeonglog.domain.pet.service.query.PetQueryService;
import com.example.harumeonglog.global.common.response.CustomResponse;
import com.example.harumeonglog.global.security.annotation.AuthenticatedMember;
import com.example.harumeonglog.global.validation.annotation.CheckCursorValidation;
import com.example.harumeonglog.global.validation.annotation.CheckSizeValidation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/api/v1/pets")
@Tag(name = "Pet", description = "Pet 관련 API")
public class PetController {

    private final PetCommandService petCommandService;
    private final PetQueryService petQueryService;

    @Operation(summary = "펫 추가 API by 백종우", description = "펫을 등록합니다. 등록하기 전에 사진을 등록해주세요. 메인 이미지는 해당 API에서 처리합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "COMMON201", description = "펫 등록 성공")
    })
    @PostMapping
    public CustomResponse<AddPetResponse> addPet(
            @RequestBody AddPetRequest request,
            @AuthenticatedMember Member member) {
        return CustomResponse.created(petCommandService.addPet(request, member));
    }

    @Operation(summary = "펫 정보 수정 API by 백종우", description = "펫 정보를 수정합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "COMMON200", description = "수정 성공")
    })
    @PatchMapping("/{petId}")
    public CustomResponse<ChangePetInfoResponse> updatePetInfo(
            @PathVariable Long petId,
            @RequestBody ChangePetInfoRequest request,
            @AuthenticatedMember Member member) {
        return CustomResponse.ok(petCommandService.changePetInfo(petId, request, member));
    }

    @Operation(summary = "펫 목록 조회 API by 백종우", description = "자신이 속한 펫 목록을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "COMMON200", description = "조회 성공")
    })
    @GetMapping
    public CustomResponse<PetResponse.GetPetsResponse> getPets(
            @RequestParam(required = false) @CheckCursorValidation Long cursor, // 커서 (마지막 펫 ID)
            @RequestParam(defaultValue = "10") @CheckSizeValidation Integer size,  // 페이지 크기
            @AuthenticatedMember Member member) {
        return CustomResponse.ok(petQueryService.getPets(cursor, size, member));
    }

    @Operation(summary = "현재 펫 변경 시 보유 펫 조회 API by 백종우",description = "대표 펫 변경 시 보유 펫 조회")
    @GetMapping("/active")
    public CustomResponse<PetResponse.PetListPreviewResponse> getActivePets(
            @RequestParam(required = false) @CheckCursorValidation Long cursor, // 커서 (마지막 펫 ID)
            @RequestParam(defaultValue = "10") @CheckSizeValidation Integer size,  // 페이지 크기
            @AuthenticatedMember Member member) {
        return CustomResponse.ok(petQueryService.getChangePet(cursor, size, member));
    }

    @Operation(summary = "현재 펫 변경 API by 백종우", description = "대표 펫을 변경합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "COMMON200", description = "변경 성공")
    })
    @PatchMapping("/{petId}/status/active")
    public CustomResponse<String> updateCurrentPet(
            @PathVariable Long petId,
            @AuthenticatedMember Member member) {
        petCommandService.changeCurrentPet(petId, member);
        return CustomResponse.ok("변경 완료");
    }

    @Operation(summary = "현재 펫 정보 조회 API by 백종우", description = "현재 선택된 펫 정보를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "COMMON200", description = "검색 성공")
    })
    @GetMapping("/active/primary")
    public CustomResponse<PetResponse.MainPetResponse> getCurrentPet(@AuthenticatedMember Member member){
        return CustomResponse.ok(petQueryService.getMainPet(member));
    }


    @Operation(summary = "펫 삭제 API by 백종우", description = "펫을 삭제합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "COMMON200", description = "삭제 성공")
    })
    @DeleteMapping("/{petId}")
    public CustomResponse<String> deletePet(@PathVariable Long petId,
                                            @AuthenticatedMember Member member) {
        petCommandService.deletePet(petId, member);
        return CustomResponse.ok("펫 삭제 완료");
    }

    @Operation(summary = "펫 초대 API by 백종우", description = "다른 사용자를 펫에 초대합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "COMMON200", description = "초대 성공")
    })
    @PostMapping("/{petId}/members")
    public CustomResponse<String> invite(
            @PathVariable Long petId,
            @RequestBody PetRequest.InviteListRequest request,
            @AuthenticatedMember Member member) {
        petCommandService.invite(petId, request, member);
        return CustomResponse.ok("초대 완료");
    }

    @Operation(summary = "멤버 검색 API by 백종우", description = "이메일로 사용자를 검색합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "COMMON200", description = "검색 성공")
    })
    @GetMapping("/members")
    public CustomResponse<PetResponse.SearchMemberResponse> searchMember(
            @RequestParam String email,
            @RequestParam(required = false) @CheckCursorValidation Long cursor,
            @RequestParam(defaultValue = "10") @CheckSizeValidation Integer size,
            @AuthenticatedMember Member member
    ) {
        PetResponse.SearchMemberResponse result = petQueryService.searchMember(email, member, cursor, size);
        return CustomResponse.ok(result);
    }



}