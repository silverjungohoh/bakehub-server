package com.project.snsserver.domain.mail.service;

import com.project.snsserver.domain.mail.model.MailMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Slf4j
@Service
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {

    @Value("${spring.mail.username}")
    private String from;

    private static final String ENCODING = "utf-8";

    private final JavaMailSender javaMailSender;


    @Override
    public boolean sendMail(MailMessage mail, String code) {
        MimeMessage message = javaMailSender.createMimeMessage();
        try {

            createMailForm(mail, message);
            javaMailSender.send(message);

        } catch (MessagingException | MailException e) {
            log.error("Fail to send email {}", e.getMessage());
            return false;
        }
        log.info("Success to send email");
        return true;
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
