package com.developmentprep.journalApp.service;

import com.developmentprep.journalApp.entity.JournalEntry;
import com.developmentprep.journalApp.entity.User;
import com.developmentprep.journalApp.repository.JournalEntryRepository;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class JournalEntryServiceTests {
    @InjectMocks
    private JournalEntryService journalEntryService;

    @Mock
    private JournalEntryRepository journalEntryRepository;

    @Mock
    private UserService userService;

    @Test
    public void testSaveNewEntry() {
        String username = "testuser";
        User user = new User();
        user.setUserName(username);
        user.setJournalEntries(new ArrayList<>());
        JournalEntry entry = new JournalEntry();
        entry.setTitle("Test Title");

        when(userService.findByUserName(username)).thenReturn(user);
        when(journalEntryRepository.save(any(JournalEntry.class))).thenReturn(entry);

        journalEntryService.saveEntry(entry, username);

        verify(journalEntryRepository, times(1)).save(entry);
        verify(userService, times(1)).saveUser(user);
        assertFalse(user.getJournalEntries().isEmpty());
        assertEquals("Test Title", user.getJournalEntries().get(0).getTitle());
    }

    @Test
    public void testFindById() {
        ObjectId id = new ObjectId();
        JournalEntry entry = new JournalEntry();
        entry.setId(id);
        when(journalEntryRepository.findById(id)).thenReturn(Optional.of(entry));

        Optional<JournalEntry> result = journalEntryService.findById(id);

        assertTrue(result.isPresent());
        assertEquals(id, result.get().getId());
    }


    @Test
    public void testDeleteById_Success() {
        String username = "testuser";
        ObjectId entryId = new ObjectId();
        User user = new User();
        user.setUserName(username);
        JournalEntry entry = new JournalEntry();
        entry.setId(entryId);
        user.setJournalEntries(new ArrayList<>());
        user.getJournalEntries().add(entry);

        when(userService.findByUserName(username)).thenReturn(user);

        boolean result = journalEntryService.deleteById(entryId, username);

        assertTrue(result);
        verify(userService, times(1)).saveUser(user);
        verify(journalEntryRepository, times(1)).deleteById(entryId);
    }

    @Test
    public void testDeleteById_Failure_EntryNotFound() {
        String username = "testuser";
        ObjectId entryId = new ObjectId();
        User user = new User();
        user.setUserName(username);
        user.setJournalEntries(new ArrayList<>());

        when(userService.findByUserName(username)).thenReturn(user);

        boolean result = journalEntryService.deleteById(entryId, username);

        assertFalse(result);
        verify(userService, never()).saveUser(user);
        verify(journalEntryRepository, never()).deleteById(entryId);
    }
}
