package com.project.snsserver.domain.security.service;

import com.project.snsserver.domain.member.model.entity.Member;
import com.project.snsserver.domain.member.repository.jpa.MemberRepository;
import com.project.snsserver.domain.security.CustomUserDetails;
import com.project.snsserver.global.error.exception.MemberException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import static com.project.snsserver.global.error.type.MemberErrorCode.MEMBER_NOT_FOUND;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

	private final MemberRepository memberRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		log.info("loadUserByUsername method call");

		Member member = memberRepository.findByEmail(username)
			.orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));

		return new CustomUserDetails(member);
	}
}
