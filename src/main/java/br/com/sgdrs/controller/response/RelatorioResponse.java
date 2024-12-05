package br.com.sgdrs.controller.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class RelatorioResponse {
    private String caminho_arquivo;
    
}
