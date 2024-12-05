package br.com.sgdrs.controller.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class EstoqueItem {
    private int quantidade;
    private String codBarras;
}



   




