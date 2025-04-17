package com.example.harumeonglog.domain.pet.repository;

import com.example.harumeonglog.domain.member.entity.Member;
import com.example.harumeonglog.domain.pet.entity.MemberPet;
import com.example.harumeonglog.domain.pet.entity.Pet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberPetRepository extends JpaRepository<MemberPet, Long> {
    Optional<MemberPet> findByMemberAndPet(Member member, Pet pet);
}
