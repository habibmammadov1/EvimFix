package com.example.evimfix.wpAdmin.Models;

import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Role {
    private String id;

    @NotBlank(message = "Role adı boş ola bilməz")
    private String name;
}
