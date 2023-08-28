package com.project.snsserver.domain.member.controller;

import com.project.snsserver.domain.member.model.dto.SendAuthCodeRequest;
import com.project.snsserver.domain.member.model.dto.SignUpRequest;
import com.project.snsserver.domain.member.model.dto.SignUpResponse;
import com.project.snsserver.domain.member.model.dto.VerifyAuthCodeRequest;
import com.project.snsserver.domain.member.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
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

    /**
     * 이메일 인증번호 발송
     */
    @Operation(summary = "이메일 인증번호 발송")
    @PostMapping("/send/email")
    public ResponseEntity<Map<String, String>> sendEmailAuthCode(@RequestBody SendAuthCodeRequest request) {

        Map<String, String> response = memberService.sendEmailAuthCode(request.getEmail());
        return ResponseEntity.ok(response);
    }

    /**
     * 이메일 인증번호 확인
     */
    @Operation(summary = "이메일 인증번호 확인")
    @PostMapping("/verify/email")
    public ResponseEntity<Map<String, String>> verifyEmailAuthCode(@RequestBody VerifyAuthCodeRequest request) {

        Map<String, String> response = memberService.verifyEmailAuthCode(request);
        return ResponseEntity.ok(response);
    }

    /**
     * 회원 가입
     */
    @Operation(summary = "회원 가입")
    @PostMapping("/sign-up")
    public ResponseEntity<SignUpResponse> signUp (@RequestPart(value = "image") MultipartFile file,
                                                  @RequestPart(value = "data") @Valid SignUpRequest request) {

        SignUpResponse response = memberService.signUp(file, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
