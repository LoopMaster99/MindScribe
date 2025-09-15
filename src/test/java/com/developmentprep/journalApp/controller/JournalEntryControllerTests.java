package com.developmentprep.journalApp.controller;

import com.developmentprep.journalApp.entity.JournalEntry;
import com.developmentprep.journalApp.entity.User;
import com.developmentprep.journalApp.service.JournalEntryService;
import com.developmentprep.journalApp.service.UserDetailsServiceImpl;
import com.developmentprep.journalApp.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@WebMvcTest(JournalEntryController.class)
public class JournalEntryControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JournalEntryService journalEntryService;

    @MockBean
    private UserService userService;

    @MockBean
    private UserDetailsServiceImpl userDetailsService;

    @TestConfiguration
    static class TestSecurityConfiguration {
        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
            http.csrf(csrf -> csrf.disable()).authorizeHttpRequests(auth -> auth.anyRequest().authenticated());
            return http.build();
        }

        @Bean
        public PasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder();
        }
    }

    @Autowired
    private ObjectMapper objectMapper;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setUserName("testuser");
        testUser.setJournalEntries(new ArrayList<>());
        when(userService.findByUserName("testuser")).thenReturn(testUser);
    }

    @Test
    @WithMockUser(username = "testuser")
    public void testGetAllJournalEntriesOfUser_Success() throws Exception {
        JournalEntry entry = new JournalEntry();
        entry.setTitle("Test Entry");
        testUser.getJournalEntries().add(entry);

        mockMvc.perform(get("/journal"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Test Entry"));
    }

    @Test
    @WithMockUser(username = "testuser")
    public void testGetAllJournalEntriesOfUser_NotFound() throws Exception {
        mockMvc.perform(get("/journal"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "testuser")
    public void testCreateEntry() throws Exception {
        JournalEntry newEntry = new JournalEntry();
        newEntry.setTitle("New Title");
        newEntry.setContent("New Content");

        mockMvc.perform(post("/journal")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newEntry)))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(username = "testuser")
    public void testGetJournalEntryById_Success() throws Exception {
        ObjectId entryId = new ObjectId();
        JournalEntry entry = new JournalEntry();
        entry.setId(entryId);
        entry.setTitle("Specific Entry");
        testUser.getJournalEntries().add(entry);

        when(journalEntryService.findById(entryId)).thenReturn(Optional.of(entry));

        mockMvc.perform(get("/journal/id/{myId}", entryId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Specific Entry"));
    }

    @Test
    @WithMockUser(username = "testuser")
    public void testGetJournalEntryById_NotFound() throws Exception {
        ObjectId entryId = new ObjectId();
        // User does not have this entry
        when(journalEntryService.findById(entryId)).thenReturn(Optional.empty());

        mockMvc.perform(get("/journal/id/{myId}", entryId))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "testuser")
    public void testDeleteJournalEntryById_Success() throws Exception {
        ObjectId entryId = new ObjectId();
        when(journalEntryService.deleteById(entryId, "testuser")).thenReturn(true);

        mockMvc.perform(delete("/journal/id/{myId}", entryId))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(username = "testuser")
    public void testUpdateJournalEntryById_Success() throws Exception {
        ObjectId entryId = new ObjectId();
        JournalEntry existingEntry = new JournalEntry();
        existingEntry.setId(entryId);
        testUser.getJournalEntries().add(existingEntry);

        JournalEntry newEntryData = new JournalEntry();
        newEntryData.setTitle("Updated Title");

        when(journalEntryService.findById(entryId)).thenReturn(Optional.of(existingEntry));

        mockMvc.perform(put("/journal/id/{id}", entryId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newEntryData)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Title"));
    }
}
