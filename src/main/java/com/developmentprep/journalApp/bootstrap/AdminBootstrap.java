package com.developmentprep.journalApp.bootstrap;

import com.developmentprep.journalApp.entity.User;
import com.developmentprep.journalApp.repository.UserRepository;
import com.developmentprep.journalApp.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class AdminBootstrap implements ApplicationRunner {

    private final UserService userService;
    private final UserRepository userRepository;

    @Override
    public void run(ApplicationArguments args) {
        // Check if any admin exists
        List<User> allUsers = userRepository.findAll();
        boolean adminExists = allUsers.stream()
                .anyMatch(user -> user.getRoles() != null && user.getRoles().contains("ADMIN"));

        if (!adminExists) {
            // Create default admin
            User admin = new User();
            admin.setUserName("admin");
            admin.setEmail("admin@journalapp.com");
            admin.setPassword("admin123"); // Will be encrypted by saveAdmin
            admin.setRoles(List.of("ADMIN"));
            admin.setSentimentAnalysis(false);

            userService.saveAdmin(admin);

            log.info("╔════════════════════════════════════════╗");
            log.info("║  Default Admin Created!                ║");
            log.info("║  Username: admin                       ║");
            log.info("║  Password: admin123                    ║");
            log.info("║  ⚠️  CHANGE PASSWORD IMMEDIATELY!      ║");
            log.info("╚════════════════════════════════════════╝");
        } else {
            log.info("Admin user already exists. Skipping admin creation.");
        }
    }
}
