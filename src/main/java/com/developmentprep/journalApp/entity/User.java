package com.developmentprep.journalApp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "users")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    private ObjectId id;

    @Indexed(unique = true)
    @NonNull
    @NotBlank(message = "Username cannot be blank")
    private String userName;

    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    @Indexed
    private String email;

    private boolean sentimentAnalysis;

    @NonNull
    @NotBlank(message = "Password cannot be blank")
    @ToString.Exclude
    @JsonIgnore
    private String password;

    @DBRef
    @Builder.Default
    private List<JournalEntry> journalEntries = new ArrayList<>();

    private List<String> roles;
}
