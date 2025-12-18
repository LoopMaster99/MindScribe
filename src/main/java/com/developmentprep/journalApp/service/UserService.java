package com.developmentprep.journalApp.service;

import com.developmentprep.journalApp.entity.JournalEntry;
import com.developmentprep.journalApp.entity.User;
import com.developmentprep.journalApp.repository.JournalEntryRepository;
import com.developmentprep.journalApp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JournalEntryRepository journalEntryRepository;

    public boolean saveNewUser(User user) {
        try {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setRoles(List.of("USER"));
            userRepository.save(user);
            return true;
        } catch (Exception e) {
            log.error("An error occured for {} :", user.getUserName(), e);
            return false;
        }
    }

    public void saveAdmin(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(List.of("ADMIN"));
        userRepository.save(user);
    }

    public void saveUser(User user) {
        userRepository.save(user);
    }

    public List<User> getAll() {
        return userRepository.findAll();
    }

    public Optional<User> findById(ObjectId id) {
        return userRepository.findById(id);
    }

    public void deleteById(ObjectId id) {
        userRepository.deleteById(id);
    }

    public User findByUserName(String username) {
        User user = userRepository.findByUserName(username);
        if (user == null) {
            throw new org.springframework.security.core.userdetails.UsernameNotFoundException(
                    "User not found: " + username);
        }
        return user;
    }

    public void deleteUserAndEntries(String username) {
        User user = findByUserName(username);

        // Delete all user's journal entries first
        for (JournalEntry entry : user.getJournalEntries()) {
            journalEntryRepository.deleteById(entry.getId());
        }

        // Then delete user
        userRepository.deleteByUserName(username);
        log.info("User {} and all their journal entries deleted successfully", username);
    }

}
// controller --> service --> repository