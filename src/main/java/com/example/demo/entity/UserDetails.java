package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.Set;


@Data
@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "UserDetails")
public class UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Size(min = 8, message = "is required")
    private String additionalEmailAddress;

    @Size(min = 3, message = "is required")
    private String city;

    @Size(min = 3, message = "is required")
    private String country;

    @Size(min = 4, message = "is required")
    private String postalCode;

    @JsonManagedReference
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private User user;

    public UserDetails(String additionalEmailAddress, String city, String country, String postalCode) {
        this.additionalEmailAddress = additionalEmailAddress;
        this.city = city;
        this.country = country;
        this.postalCode = postalCode;

    }

    public void setUser(Integer id, String firstName, String lastName,
                        String email, String password, boolean termsAccepted,
                        Set<Workspace> workspaces) {
        user.setId(id);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setPassword(password);
        user.setTermsAccepted(termsAccepted);
        user.setWorkspaces(workspaces);

    }
}
