package com.example.demo.model;

import com.example.demo.entity.UserDetails;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;



@AllArgsConstructor
@Builder
@Data
@Setter
@Getter
public class EntityResponseDto {

    private int code;
    private String message;
    private Object data;

}
