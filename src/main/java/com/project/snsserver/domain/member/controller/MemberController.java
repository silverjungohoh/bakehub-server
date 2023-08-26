package com.project.snsserver.domain.member.controller;

import com.project.snsserver.domain.member.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/members")
@RequiredArgsConstructor
@Tag(name = "회원", description = "회원 API Document")
public class MemberController {

    private final MemberService memberService;

    /**
     * 이메일 중복 확인
     */
    @Operation(summary = "이메일 중복 확인")
    @GetMapping("/duplicate/email/{email}")
    public ResponseEntity<Map<String, String>> checkEmailDuplicate(@PathVariable String email) {

        Map<String, String> response = memberService.checkEmailDuplicate(email);
        return ResponseEntity.ok(response);
    }

    /**
     * 닉네임 중복 확인
     */
    @Operation(summary = "닉네임 중복 확인")
    @GetMapping("/duplicate/nickname/{nickname}")
    public ResponseEntity<Map<String, String>> checkNicknameDuplicate(@PathVariable String nickname) {

        Map<String, String> response = memberService.checkNicknameDuplicate(nickname);
        return ResponseEntity.ok(response);
    }
}
