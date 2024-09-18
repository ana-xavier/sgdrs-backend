package br.com.sgdrs.controller.request;

import br.com.sgdrs.domain.enums.TipoUsuario;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
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
    private String nome;

    @NotBlank(message = "Email não pode ser vazio")
    @Email(message = "Email inválido informado")
    private String email;

    @NotBlank(message = "Senha não pode ser vazia")
    private String senha;

    @Enumerated(STRING)
    private TipoUsuario tipoUsuario;
}
