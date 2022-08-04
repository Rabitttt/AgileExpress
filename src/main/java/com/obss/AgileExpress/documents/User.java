package com.obss.AgileExpress.documents;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Document("users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    private String id;
    @Indexed(unique = true)
    private String username;
    @Indexed(unique = true)
    private String email;
    private String password;
    private Collection<String> roles = new ArrayList<>();

    /*
    @DocumentReference
    @JsonBackReference
    private List<Project> projects = new ArrayList<>();
     */

    /*
    @DocumentReference(lazy = true)
    private List<Project> projects = new ArrayList<>();
    @DocumentReference(lazy = true)
    private List<Task> tasks = new ArrayList<>();
     */
}
