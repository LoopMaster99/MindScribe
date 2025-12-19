package com.developmentprep.journalApp.controller;

import com.developmentprep.journalApp.api.response.WeatherResponse;
import com.developmentprep.journalApp.dto.EmailRequest;
import com.developmentprep.journalApp.entity.User;
import com.developmentprep.journalApp.service.EmailService;
import com.developmentprep.journalApp.service.UserService;
import com.developmentprep.journalApp.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private WeatherService weatherService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private org.springframework.security.crypto.password.PasswordEncoder passwordEncoder;

    @PostMapping("/send-email")
    public ResponseEntity<?> sendEmail(@Valid @RequestBody EmailRequest emailRequest) {
        try {
            // Get authenticated user's email for Reply-To
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            User user = userService.findByUserName(username);

            // Send email with user's email as Reply-To
            emailService.sendMail(user.getEmail(), emailRequest.getTo(),
                    emailRequest.getSubject(), emailRequest.getBody());

            return new ResponseEntity<>("Email sent successfully to " + emailRequest.getTo(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to send email: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping
    public ResponseEntity<?> updateUser(@Valid @RequestBody User user) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String username = authentication.getName();
        User userInDb = userService.findByUserName(username);

        // Only update email, password, and sentimentAnalysis - NOT username (to avoid
        // breaking JWT auth) and NOT roles (to preserve ADMIN/USER status)
        if (user.getEmail() != null && !user.getEmail().isEmpty()) {
            userInDb.setEmail(user.getEmail());
        }
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            userInDb.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        userInDb.setSentimentAnalysis(user.isSentimentAnalysis());

        userService.saveUser(userInDb);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteByUserName() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        // Prevent admin users from deleting their accounts
        User user = userService.findByUserName(username);
        if (user.getRoles() != null && user.getRoles().contains("ADMIN")) {
            return new ResponseEntity<>("Admin users cannot delete their accounts", HttpStatus.FORBIDDEN);
        }

        userService.deleteUserAndEntries(username);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{city}")
    public ResponseEntity<?> getWelcomeMessage(@PathVariable String city) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        WeatherResponse weatherResponse = weatherService.getWeather(city);
        String weatherInfo = "";
        if (weatherResponse != null) {
            weatherInfo = " Today's weather in " + city + " feels like " + weatherResponse.getCurrent().getFeelslike()
                    + "°C.";
        }
        return new ResponseEntity<>("Welcome back, " + username + "! \n" + weatherInfo, HttpStatus.OK);
    }

}
