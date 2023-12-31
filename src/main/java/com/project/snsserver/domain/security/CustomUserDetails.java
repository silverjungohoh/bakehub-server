package com.project.snsserver.domain.security;

import com.project.snsserver.domain.member.model.entity.Member;
import com.project.snsserver.domain.member.type.MemberRole;
import com.project.snsserver.domain.member.type.MemberStatus;

import lombok.Getter;
import lombok.NoArgsConstructor;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

@Getter
@NoArgsConstructor
public class CustomUserDetails implements UserDetails {

	private String username;

	private String password;

	private MemberRole role;

	private Member member;

	public CustomUserDetails(Member member) {
		this.username = member.getEmail();
		this.password = member.getPassword();
		this.member = member;
		this.role = member.getRole();
	}

	// 계정의 권한 목록
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return Collections.singleton(new SimpleGrantedAuthority(role.getValue()));
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return username;
	}

	// 계정의 만료 여부
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	// 계정의 잠금 여부
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	// 비밀번호 만료 여부
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	// 계정의 활성화 여부
	@Override
	public boolean isEnabled() {
		return Objects.equals(MemberStatus.ACTIVE, member.getStatus());
	}
}
