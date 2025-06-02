package com.example.evimfix.wpAdmin.Models;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Auth {
    private int id;
    @NotBlank(message = "İstifadəçi adı boş ola bilməz.")
    @Size(min = 1, max = 15, message = "İstifadəçi adı min. 3 simvoldan ibarət olmalıdır.")
    private String username;

    @NotBlank(message = "Adı boş ola bilməz.")
    @Size(min = 2, max = 15, message = "Adı min. 2 simvoldan ibarət olmalıdır.")
    private String firstName;

    @NotBlank(message = "Soyadı boş ola bilməz.")
    @Size(min = 2, max = 15, message = "Adı min. 2 simvoldan ibarət olmalıdır.")
    private String lastName;

    @NotBlank(message = "Email boş ola bilməz.")
    @Email(message = "Email tipində daxil edin.")
    @Size(min = 7, max = 100, message = "Email min. 7 simvoldan ibarət olmalıdır.")
    private String email;

    @NotBlank(message = "Parol boş ola bilməz.")
    @Size(min = 6, max = 100, message = "Parol min. 6 simvoldan ibarət olmalıdır.")
    private String password;

    @NotBlank(message = "Rol boş ola bilməz.")
    private String role;

    private boolean enabled;
    
    private String roleName;
}
