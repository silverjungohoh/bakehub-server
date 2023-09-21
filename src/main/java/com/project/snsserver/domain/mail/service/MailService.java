package com.project.snsserver.domain.mail.service;

import com.project.snsserver.domain.mail.model.MailMessage;

public interface MailService {

	/**
	 * 이메일 전송
	 */
	boolean sendMail(MailMessage mail, String code);
}
