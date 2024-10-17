package br.com.sgdrs.mapper;

import br.com.sgdrs.controller.request.IncluirUsuarioRequest;
import br.com.sgdrs.controller.response.UsuarioResponse;
import br.com.sgdrs.domain.Abrigo;
import br.com.sgdrs.domain.CentroDistribuicao;
import br.com.sgdrs.domain.Usuario;

import java.util.ArrayList;
import java.util.Optional;

public class UsuarioMapper {
    public static Usuario toEntity(IncluirUsuarioRequest request) {
        return Usuario.builder()
                .nome(request.getNome())
                .email(request.getEmail())
                .tipo(request.getTipo())
                .permissoes(new ArrayList<>())
                .build();
    }

    public static UsuarioResponse toResponse(Usuario usuario) {
        return UsuarioResponse.builder()
                .id(usuario.getId())
                .email(usuario.getEmail())
                .nome(usuario.getNome())
                .tipo(usuario.getTipo())
                .ativo(usuario.isAtivo())
                .id_abrigo(Optional.ofNullable(usuario.getAbrigo()).map(Abrigo::getId).orElse(null))
                .id_centroDistribuicao(Optional.ofNullable(usuario.getCentroDistribuicao()).map(CentroDistribuicao::getId).orElse(null))
                .build();
    }
}
