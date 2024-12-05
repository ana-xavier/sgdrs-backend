package br.com.sgdrs.domain.Relatorio;

import java.time.LocalDate;
import java.util.Date;

import lombok.*;


@Getter
@Setter
@Builder
public class Relatorio {
    private String nome_doador;
    private String cpf_cnpj_doador;
    private String nome_item;
    private String categoria_item;
    private int valor_por_unidade;
    private String unidade_item;
    private String cod_barras_item;
    private int quantidade_doada;   
    private LocalDate data;
}
