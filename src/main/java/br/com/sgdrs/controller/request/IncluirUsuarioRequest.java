package br.com.sgdrs.controller.request;

import br.com.sgdrs.domain.enums.TipoUsuario;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import static jakarta.persistence.EnumType.STRING;

@ToString
@Getter
@Setter
public class IncluirUsuarioRequest {
   @NotBlank
   @Pattern(regexp = "^[A-Za-zÀ-ÖØ-öø-ÿ ]+$", message = "Nome deve conter apenas letras e espaços")
    private String nome;

   @NotBlank
    @Email
    private String email;

  @NotBlank
    private String senha;

   @Enumerated(STRING)
    private TipoUsuario tipoUsuario;
}
