package com.example.harumeonglog.domain.walk.service;

import com.example.harumeonglog.domain.member.domain.Member;
import com.example.harumeonglog.domain.pet.domain.Pet;
import com.example.harumeonglog.domain.walk.controller.dto.request.MemberWalkRequest;
import com.example.harumeonglog.domain.walk.controller.port.MemberWalkService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MemberWalkServiceImpl implements MemberWalkService {
    @Override
    public List<Pet> getPets(Member member) {
        return List.of();
    }

    @Override
    public List<Member> getMembers(MemberWalkRequest.PetMemberRequest dto) {
        return List.of();
    }
}
