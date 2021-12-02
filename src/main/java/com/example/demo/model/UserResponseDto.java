package com.example.demo.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDto {

    private Integer id;
    private String firstName;
    private String lastName;
    private String email;

    @JsonIgnore
    private String password;

    private boolean termsAccepted;
    private UserDetailsDto userDetails;

}

