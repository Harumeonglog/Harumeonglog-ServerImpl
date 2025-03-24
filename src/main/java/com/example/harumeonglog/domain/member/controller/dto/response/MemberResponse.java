package com.example.harumeonglog.domain.member.controller.dto.response;

import com.example.harumeonglog.domain.member.domain.Member;
import lombok.Builder;
import lombok.Getter;

public class MemberResponse {

    @Getter
    @Builder
    public static class MemberInfoResponse {
        private Long memberId;
        private String email;
        private String nickname;
        private String image;

        public static MemberInfoResponse from(Member member) {
            return MemberInfoResponse.builder()
                    .memberId(member.getId())
                    .email(member.getEmail())
                    .nickname(member.getNickname())
                    .image(member.getImage())
                    .build();
        }
    }
}
