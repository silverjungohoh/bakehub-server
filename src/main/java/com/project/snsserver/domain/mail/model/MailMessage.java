package com.project.snsserver.domain.mail.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MailMessage {

    private String to;

    private String subject;

    private String message;
}
