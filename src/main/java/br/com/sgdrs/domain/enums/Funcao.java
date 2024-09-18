package br.com.sgdrs.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Funcao {

    VOLUNTARIO("ROLE_VOLUNTARIO"),
    ADMIN("ROLE_ADMIN"),
    SUPERADMIN("ROLE_SUPERADMIN");

    private final String role;
}
