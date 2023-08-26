package com.project.snsserver.domain.member.service;

import com.project.snsserver.domain.member.repository.MemberRepository;
import com.project.snsserver.global.error.exception.MemberException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

import static com.project.snsserver.global.error.type.MemberErrorCode.DUPLICATED_EMAIL;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;


    @Override
    public Map<String, String> checkEmailDuplicate(String email) {

        if(memberRepository.existsByEmail(email)) {
            throw new MemberException(DUPLICATED_EMAIL);
        }
        return getMessage("사용 가능한 이메일입니다.");
    }

    private static Map<String, String> getMessage(String message) {
        Map<String, String> result = new HashMap<>();
        result.put("result", message);
        return result;
    }
}
