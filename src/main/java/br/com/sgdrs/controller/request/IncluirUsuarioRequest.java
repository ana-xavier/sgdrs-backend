package br.com.sgdrs.controller.request;

import br.com.sgdrs.domain.enums.TipoUsuario;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import static jakarta.persistence.EnumType.STRING;

@ToString
@Getter
@Setter
public class IncluirUsuarioRequest {
    @NotBlank(message = "Nome não pode ser vazio")
    @Pattern(regexp = "^[A-Za-zÀ-ÖØ-öø-ÿ ]+$", message = "Nome deve conter apenas letras e espaços")
    @Size(max = 250, message = "Nome deve ter até 250 caracteres")
    private String nome;

    @NotBlank(message = "Email não pode ser vazio")
    @Email(message = "Email inválido informado")
    @Size(max = 250, message = "Email deve ter até 250 caracteres")
    private String email;

    // @NotBlank(message = "Senha não pode ser vazia")
    // @Size(max = 250, message = "Senha deve ter até 250 caracteres")
    // private String senha;

    @Enumerated(STRING)
    @NotNull(message = "Tipo do usuário não deve ser nulo")
    private TipoUsuario tipo;
}
