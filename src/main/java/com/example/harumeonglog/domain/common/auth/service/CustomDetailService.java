package com.example.harumeonglog.domain.common.auth.service;

import com.example.harumeonglog.domain.common.auth.domain.CustomUserDetails;
import com.example.harumeonglog.domain.member.domain.Member;
import com.example.harumeonglog.domain.member.exception.MemberErrorCode;
import com.example.harumeonglog.domain.member.exception.MemberException;
import com.example.harumeonglog.domain.member.service.port.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomDetailService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = memberRepository.findByEmail(username).orElseThrow(() -> new MemberException(MemberErrorCode.NOT_FOUND)).toModel();
        return new CustomUserDetails(member);
    }
}
