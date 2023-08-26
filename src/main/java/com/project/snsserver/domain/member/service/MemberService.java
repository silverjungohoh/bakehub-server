package com.project.snsserver.domain.member.service;

import java.util.Map;

public interface MemberService {

    /**
     * 이메일 중복 확인
     */
    Map<String, String> checkEmailDuplicate(String email);

}
