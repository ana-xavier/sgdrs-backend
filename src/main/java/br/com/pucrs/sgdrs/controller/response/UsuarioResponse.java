package br.com.pucrs.sgdrs.controller.response;

import br.com.pucrs.sgdrs.domain.enums.TipoUsuario;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioResponse {
    private UUID id;
    private String email;
    private String nome;
    private TipoUsuario tipoUsuario;
}
