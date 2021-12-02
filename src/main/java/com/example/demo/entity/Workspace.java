package com.example.demo.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

@Setter
@Getter
@RequiredArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Workspaces")
public class Workspace {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)   //IDENTITY
    private long id;

    @NonNull
    @NotEmpty
    private String name;

    @NonNull
    private Integer coordinates;

    @NonNull
    private Integer seats;

    @JsonBackReference
    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")  //name je column name iz user tabele za id
    private User user;

    public Workspace merge(@NonNull Workspace otherWorkspace) {
        setId(otherWorkspace.getId());
        setName(otherWorkspace.getName());
        setCoordinates(otherWorkspace.getCoordinates());
        setSeats(otherWorkspace.getSeats());

        return this;
    }
}
