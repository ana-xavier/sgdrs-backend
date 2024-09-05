package br.com.pucrs.sgdrs.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Funcao {

    VOLUNTARIO(Nomes.VOLUNTARIO),
    ADMIN(Nomes.ADMIN),
    SUPERADMIN(Nomes.SUPERADMIN);


    public static class Nomes {
        public static final String VOLUNTARIO = "ROLE_VOLUNTARIO";
        public static final String ADMIN = "ROLE_ADMIN";
        public static final String SUPERADMIN = "ROLE_SUPERADMIN";
    }

    private final String role;
}
