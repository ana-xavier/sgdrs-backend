package br.com.sgdrs.controller.response;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemVerificadoResponse{
    private ItemResponse item;
    private boolean status; 
}
