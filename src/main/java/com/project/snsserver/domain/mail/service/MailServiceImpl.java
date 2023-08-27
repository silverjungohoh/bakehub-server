package com.project.snsserver.domain.mail.service;

import com.project.snsserver.domain.mail.model.MailMessage;
import com.project.snsserver.global.error.exception.MemberException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import static com.project.snsserver.global.error.type.MemberErrorCode.FAIL_TO_SEND_EMAIL;

@Slf4j
@Service
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {

    @Value("${spring.mail.username}")
    private String from;

    private static final String ENCODING = "utf-8";

    private final JavaMailSender javaMailSender;


    @Override
    public void sendMail(MailMessage mail, String code) {
        MimeMessage message = javaMailSender.createMimeMessage();
        try {

            createMailForm(mail, message);
            javaMailSender.send(message);

        } catch (MessagingException e) {
            log.error("Fail to send email {}", e.getMessage());
            throw new MemberException(FAIL_TO_SEND_EMAIL);
        }
        log.info("Success to send email");
    }

    /**
     * 메일 양식 생성
     */
    private void createMailForm(MailMessage mail, MimeMessage message) throws MessagingException {
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(message, true, ENCODING);

        mimeMessageHelper.setFrom(from);
        mimeMessageHelper.setTo(mail.getTo());
        mimeMessageHelper.setSubject(mail.getSubject());
        mimeMessageHelper.setText(mail.getMessage(), true);
    }
}
