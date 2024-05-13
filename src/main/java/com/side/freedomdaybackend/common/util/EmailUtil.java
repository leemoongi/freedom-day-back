package com.side.freedomdaybackend.common.util;

import com.side.freedomdaybackend.common.constants.Constants;
import com.side.freedomdaybackend.common.exception.CustomException;
import com.side.freedomdaybackend.common.exception.ErrorCode;
import com.side.freedomdaybackend.domain.member.dto.EmailAuthenticationDto;
import io.jsonwebtoken.Claims;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.time.Duration;
import java.util.HashMap;

@Component
@RequiredArgsConstructor
public class EmailUtil {

    @Value("${spring.mail.username}")
    private String serviceEmail;

    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;

    private final JwtUtil jwtUtil;
    private final RedisUtil redisUtil;

    private static String EMAIL_URL = "https://www.freedom-day.site/api/member/email-authentication?token=";

    public void sendAuthUrlMail(EmailAuthenticationDto dto) throws MessagingException {
        String email = dto.getEmail();

        String emailAuthToken = jwtUtil.createEmailAuthToken(email);
        redisUtil.set("emailAUth-" + email , Constants.EMAIL_NOT_AUTHENTICATED, Duration.ofHours(1).toMillis());

        HashMap<String, Object> templateModel = new HashMap<>();
        templateModel.put("url", EMAIL_URL + emailAuthToken);
//        templateModel.put("url", "http://localhost:8080/api/member/email-authentication?token=" + emailAuthToken);

        String subject = String.format("해방의날");
        Context thymeleafContext = new Context();
        thymeleafContext.setVariables(templateModel);
        String htmlBody = templateEngine.process("email-authentication.html", thymeleafContext);

        sendHtmlMessage(email, subject, htmlBody);
    }

    private void sendHtmlMessage(String to, String subject, String htmlBody) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setFrom(serviceEmail);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlBody, true);

        mailSender.send(message);
    }

    public void emailAuthentication(String token) {
        Claims claims = jwtUtil.isValidToken(token);
        String email = claims.get(Constants.EMAIL).toString();

        if(!redisUtil.exists("emailAUth-" + email)) {
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR); // TODO) 존재하지 않을때
        }

        redisUtil.update("emailAUth-" + email, Constants.EMAIL_AUTHENTICATED);
    }
}