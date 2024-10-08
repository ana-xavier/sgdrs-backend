package br.com.sgdrs.service.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

import br.com.sgdrs.domain.Usuario;
import br.com.sgdrs.domain.enums.TipoUsuario;
import br.com.sgdrs.repository.IUsuarioRepository;
import jakarta.annotation.PostConstruct;

@Component
public class InitialDataLoader {

    @Autowired
    private IUsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostConstruct
    public void init() {

        Optional<Usuario> superAdminExistente = usuarioRepository.findByEmail("sadm@email.com");

        if (superAdminExistente.isEmpty()) {
            // Se não existir, cria o superadmin
            Usuario superAdmin = Usuario.builder()
                    .id(UUID.randomUUID())
                    .nome("Super Admin")
                    .email("sadm@email.com")
                    .senha(getSenhaCriptografada("1234"))
                    .tipo(TipoUsuario.SUPERADMIN)
                    .ativo(true)
                    .build();

            usuarioRepository.save(superAdmin);
        }
    }

    private String getSenhaCriptografada(String senha) {
        return passwordEncoder.encode(senha);
    }
}
