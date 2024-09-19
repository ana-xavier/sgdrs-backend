package br.com.sgdrs.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Funcao {

    VOLUNTARIO("ROLE_VOLUNTARIO"),
    ADMIN_CD("ROLE_ADMIN_CD"),
    ADMIN_ABRIGO("ROLE_ADMIN_ABRIGO"),
    SUPERADMIN("ROLE_SUPERADMIN");

    private final String role;
}
