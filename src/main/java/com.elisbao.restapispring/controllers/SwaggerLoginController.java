package com.elisbao.restapispring.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.constraints.NotBlank;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class SwaggerLoginController {

    // Modelo para o corpo da requisição
    public static class LoginRequest {
        @NotBlank
        @Parameter(description = "Nome de usuário", required = true)
        private String username;

        @NotBlank
        @Parameter(description = "Senha do usuário", required = true)
        private String password;

        // Getters e Setters
        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }

    @Operation(
            summary = "Login",
            description = "Autenticação de usuário para obter o token JWT",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Token JWT recebido com sucesso"),
                    @ApiResponse(responseCode = "400", description = "Credenciais inválidas")
            }
    )
    @PostMapping("/login")
    public void login(@RequestBody LoginRequest loginRequest) {
        // Este método não faz nada, pois estamos apenas documentando o endpoint para o Swagger
    }
}
