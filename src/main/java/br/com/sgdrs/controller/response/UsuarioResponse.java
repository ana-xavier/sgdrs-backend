package br.com.sgdrs.controller.response;

import br.com.sgdrs.domain.enums.TipoUsuario;
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
    private TipoUsuario tipo;
    private UUID id_abrigo;
    private UUID id_centroDistribuicao;
    private boolean ativo;
}
