package com.developmentprep.journalApp.controller;

import com.developmentprep.journalApp.cache.AppCache;
import com.developmentprep.journalApp.dto.UserDTO;
import com.developmentprep.journalApp.entity.User;
import com.developmentprep.journalApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private AppCache appCache;

    @GetMapping("/all-users")
    public ResponseEntity<?> getAllUsers() {
        List<UserDTO> all = userService.getAllUser();
        if (all != null && !all.isEmpty()) {
            return new ResponseEntity<>(all, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/create-admin-user")
    public void createUser(@Valid @RequestBody User user) {
        userService.saveAdmin(user);
    }

    @PostMapping("/clear-app-cache")
    public void clearAppCache() {
        appCache.init();
    }
}
