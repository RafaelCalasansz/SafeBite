package com.safebite.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class UserRegisterDTO {
    @NotEmpty(message = "Nome não pode ser vazio")
    private String nome;

    @Email(message = "Email inválido")
    @NotEmpty(message = "Email não pode ser vazio")
    private String email;

    @NotEmpty(message = "Senha não pode ser vazia")
    private String password;
}