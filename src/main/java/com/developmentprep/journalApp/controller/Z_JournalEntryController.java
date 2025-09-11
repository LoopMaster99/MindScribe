/*
package com.developmentprep.journalApp.controller;

import com.developmentprep.journalApp.entity.Z_JournalEntry;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/_journal")
public class Z_JournalEntryController {

    private Map<Long, Z_JournalEntry> journalEntries = new HashMap<>();

    @GetMapping
    public List<Z_JournalEntry> getAll(){
        return new ArrayList<>(journalEntries.values());
    }

    @PostMapping
    public boolean createEntry(@RequestBody Z_JournalEntry myEntry){
        journalEntries.put(myEntry.getId(), myEntry);

        return true;
    }

    @GetMapping("id/{myId}")
    public Z_JournalEntry getJournalEntryById(@PathVariable Long myId){
        return journalEntries.get(myId);
    }

    @DeleteMapping("id/{myId}")
    public Z_JournalEntry deleteJournalEntryById(@PathVariable Long myId){
        return journalEntries.remove(myId);
    }

    @PutMapping("id/{id}")
    public Z_JournalEntry updateJournalEntryById(@PathVariable Long id, @RequestBody Z_JournalEntry myEntry){
        return journalEntries.put(id, myEntry);
    }
}
*/
