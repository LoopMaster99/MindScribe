package com.developmentprep.journalApp.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * SendGrid configuration properties
 */
@Configuration
@ConfigurationProperties(prefix = "sendgrid")
@Data
public class SendGridConfig {
    private String apiKey;
    private String fromEmail;
    private String fromName;
}
