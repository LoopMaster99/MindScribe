package com.developmentprep.journalApp.controller;

import com.developmentprep.journalApp.entity.User;
import com.developmentprep.journalApp.repository.UserRepository;
import com.developmentprep.journalApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

     @PutMapping
     public ResponseEntity<?> updateUser(@RequestBody User user){
         Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

         String username = authentication.getName();
         User userInDb = userService.findByUserName(username);

         userInDb.setUserName(user.getUserName());
         userInDb.setPassword(user.getPassword());
         userService.saveNewUser(userInDb);

         return new ResponseEntity<>(HttpStatus.NO_CONTENT);
     }

     @DeleteMapping
     public ResponseEntity<?> deleteByUserName(){
         Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
         userRepository.deleteByUserName(authentication.getName());
         return new ResponseEntity<>(HttpStatus.NO_CONTENT);
     }

}
