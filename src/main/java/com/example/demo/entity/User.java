package com.example.demo.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.Set;

@NoArgsConstructor
@Setter
@Getter
@RequiredArgsConstructor
@Entity
@Table(name = "Users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @NonNull
    @NotEmpty
    @Size(min = 3, message = "is required")
    private String firstName;

    @NonNull
    @NotEmpty
    @Size(min = 3, message = "is required")
    private String lastName;

    @NonNull
    @NotEmpty
    @Size(min = 8, message = "is required")
    @Column(name = "email", unique = true, length = 50)
    private String email;

    @NonNull
    @NotEmpty
    @JsonIgnore
    String password;

    @AssertTrue
    private boolean termsAccepted;

    //@JsonIgnore
    @JsonBackReference
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_details_id")                 //defines a foreign key column
    private UserDetails userDetails;

    @JsonManagedReference
    @OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private Set<Workspace> workspaces;

    public User(String firstName, String lastName, String email, String password, boolean termsAccepted, UserDetails userDetails) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.termsAccepted = termsAccepted;
        this.userDetails.setUser(this);
    }

}

