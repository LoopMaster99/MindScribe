package com.developmentprep.journalApp.service;

import com.developmentprep.journalApp.config.SendGridConfig;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    private final SendGridConfig sendGridConfig;

    public void sendMail(String to, String subject, String body) {
        sendMail(null, to, subject, body);
    }

    public void sendMail(String replyTo, String to, String subject, String body) {
        try {
            Email from = new Email(sendGridConfig.getFromEmail(), sendGridConfig.getFromName());
            Email toEmail = new Email(to);
            Content content = new Content("text/plain", body);
            Mail mail = new Mail(from, subject, toEmail, content);

            // Set reply-to if provided
            if (replyTo != null && !replyTo.isEmpty()) {
                Email replyToEmail = new Email(replyTo);
                mail.setReplyTo(replyToEmail);
            }

            SendGrid sg = new SendGrid(sendGridConfig.getApiKey());
            Request request = new Request();

            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());

            Response response = sg.api(request);

            if (response.getStatusCode() >= 200 && response.getStatusCode() < 300) {
                log.info("Email sent successfully to: {} (Status: {})", to, response.getStatusCode());
            } else {
                log.error("Failed to send email to: {}. Status: {}, Body: {}",
                        to, response.getStatusCode(), response.getBody());
            }

        } catch (IOException e) {
            log.error("Failed to send email to: {}", to, e);
            throw new RuntimeException("Email sending failed: " + e.getMessage(), e);
        }
    }
}
