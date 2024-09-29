package br.com.sgdrs.controller.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Builder
public class CentroDistribuicaoResponse {
    private UUID id;
}
