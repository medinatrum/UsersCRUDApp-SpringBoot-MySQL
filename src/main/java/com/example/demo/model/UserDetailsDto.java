package com.example.demo.model;

import com.example.demo.entity.UserDetails;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDetailsDto {

    private Integer id;
    private String additionalEmailAddress;
    private String city;
    private String country;
    private String postalCode;

}
