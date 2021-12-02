package com.example.demo.model;

import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class LoginRequestDto {

    @NonNull
    @NotEmpty
    @Size(min=8, message="is required")
    private String email;

    @NotEmpty
    @Size(min=3, message="is required")
    @NonNull
    private String password;

}
