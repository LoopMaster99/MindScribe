package com.developmentprep.journalApp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;

import java.util.List;

/**
 * Data Transfer Object for User entity.
 * Used to expose user information without sensitive data like journal entries.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private ObjectId id;
    private String userName;
    private String email;
    private boolean sentimentAnalysis;
    private List<String> roles;
}
