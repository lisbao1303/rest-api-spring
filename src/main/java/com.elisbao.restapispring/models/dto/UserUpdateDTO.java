package com.elisbao.restapispring.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserUpdateDTO {

    private Long id;

    @NotBlank
    @Size(min = 8, max = 60)
    private String password;

}
