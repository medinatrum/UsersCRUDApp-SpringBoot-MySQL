package com.example.demo.model;

import com.example.demo.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkspaceDto {

    private Long id;

    @NonNull
    @NotEmpty
    private String name;

    @NonNull
    @NotEmpty
    private Integer coordinates;

    @NonNull
    private Integer seats;

}
