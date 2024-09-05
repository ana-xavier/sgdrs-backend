package br.com.pucrs.sgdrs.mapper;

import br.com.pucrs.sgdrs.controller.request.IncluirUsuarioRequest;
import br.com.pucrs.sgdrs.controller.response.UsuarioResponse;
import br.com.pucrs.sgdrs.domain.Usuario;

import java.util.ArrayList;

public class UsuarioMapper {
    public static Usuario toEntity(IncluirUsuarioRequest request) {
        return Usuario.builder()
                .nome(request.getNome())
                .email(request.getEmail())
                .tipo(request.getTipoUsuario())
                .permissoes(new ArrayList<>())
                .build();
    }

    public static UsuarioResponse toResponse(Usuario usuario) {
        return UsuarioResponse.builder()
                .id(usuario.getId())
                .email(usuario.getEmail())
                .nome(usuario.getNome())
                .tipoUsuario(usuario.getTipo())
                .build();
    }
}
