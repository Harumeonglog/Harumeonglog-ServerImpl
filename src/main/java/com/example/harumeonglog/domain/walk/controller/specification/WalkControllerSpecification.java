package com.example.harumeonglog.domain.walk.controller.specification;

import com.example.harumeonglog.domain.member.entity.Member;
import com.example.harumeonglog.domain.walk.dto.request.WalkRequest;
import com.example.harumeonglog.domain.walk.dto.response.WalkResponse;
import com.example.harumeonglog.global.common.response.CustomResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

public interface WalkControllerSpecification {

    @Operation(summary = "산책 가능한 펫 API", description = "산책 가능한 펫들 가져오는 API")
    @ApiResponse(responseCode = "COMMON200", description = "성공입니다.")
    CustomResponse<WalkResponse.WalkAvailablePetListResponse> getAvailablePetList(Member member);

    @Operation(summary = "산책 가능한 사용자 API", description = "산책 가능한 사용자 가져오는 API")
    @ApiResponse(responseCode = "COMMON200", description = "성공입니다.")
    CustomResponse<WalkResponse.WalkAvailableMemberListResponse> getAvailableMemberList(WalkRequest.AvailableMemberRequest request);
}
