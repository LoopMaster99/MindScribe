package com.developmentprep.journalApp.repository;

import com.developmentprep.journalApp.entity.JournalEntry;
import com.developmentprep.journalApp.enums.Sentiment;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface JournalEntryRepository extends MongoRepository<JournalEntry, ObjectId> {

    List<JournalEntry> findByDateBetween(LocalDateTime start, LocalDateTime end);

    List<JournalEntry> findBySentiment(Sentiment sentiment);

    long countBySentiment(Sentiment sentiment);
}
