 package br.com.sgdrs.mapper;

import br.com.sgdrs.controller.response.RelatorioResponse;

public class RelatorioMapper {
    public static RelatorioResponse toResponse(String caminho_arquiivo){
        return RelatorioResponse.builder()
            .caminho_arquivo(caminho_arquiivo)
            .build();
    }

    
}