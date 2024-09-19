package br.com.sgdrs.dto;

import br.com.sgdrs.domain.Endereco;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CentroDistribuicaoDTO {
    private String nome;
    private Endereco endereco;
}
