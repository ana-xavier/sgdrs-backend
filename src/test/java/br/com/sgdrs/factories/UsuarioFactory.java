package br.com.sgdrs.factories;

import br.com.sgdrs.domain.Usuario;
import br.com.sgdrs.domain.enums.TipoUsuario;

import static br.com.sgdrs.domain.enums.TipoUsuario.*;

public class UsuarioFactory {
    public static Usuario.UsuarioBuilder get(){
        return Usuario.builder()
                .id(UtilsFactory.getRandomUUID())
                .nome("jorge")
                .email("jorge@email.com")
                .abrigo(null)
                .centroDistribuicao(null)
                .ativo(true)
                .senha("1234");
    }

    public static Usuario getVoluntario(){
        return get()
                .tipo(VOLUNTARIO)
                .build();
    }

    public static Usuario getAdminAbrigo(){
        return get()
                .tipo(ADMIN_ABRIGO)
                .build();
    }

    public static Usuario getAdminCD(){
        return get()
                .tipo(ADMIN_CD)
                .build();
    }

    public static Usuario getSuperadmin(){
        return get()
                .tipo(SUPERADMIN)
                .build();
    }
}
