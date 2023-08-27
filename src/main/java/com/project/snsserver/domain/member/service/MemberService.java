package com.project.snsserver.domain.member.service;

import java.util.Map;

public interface MemberService {

    /**
     * 이메일 중복 확인
     */
    Map<String, String> checkEmailDuplicate(String email);

    /**
     * 닉네임 중복 확인
     */
    Map<String, String> checkNicknameDuplicate(String nickname);

    /**
     * 이메일 인증번호 전송
     */
    Map<String, String> sendEmailAuthCode(String email);
}
