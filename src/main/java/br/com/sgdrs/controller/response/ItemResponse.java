package br.com.sgdrs.controller.response;

import lombok.*;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemResponse {
    private UUID id;
    private String nome;
    private String descricao;
    private int quantidade;
    private String categoria;
    private String codBarras;
    private boolean validado;


    
}
