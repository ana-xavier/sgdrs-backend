package br.com.sgdrs.service.util;

import br.com.sgdrs.domain.Permissao;
import br.com.sgdrs.domain.enums.Funcao;
import br.com.sgdrs.repository.PermissaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Optional;

import br.com.sgdrs.domain.Usuario;
import br.com.sgdrs.domain.enums.TipoUsuario;
import br.com.sgdrs.repository.UsuarioRepository;
import jakarta.annotation.PostConstruct;

@Component
public class InitialDataLoader {

   @Autowired
   private UsuarioRepository usuarioRepository;

   @Autowired
   private PermissaoRepository permissaoRepository;

   @Autowired
   private PasswordEncoder passwordEncoder;

   @PostConstruct
   public void init() {

       Optional<Usuario> superAdminExistente = usuarioRepository.findByEmail("sadm@email.com");

       if (superAdminExistente.isEmpty()) {
           // Se não existir, cria o superadmin
           Usuario superAdmin = Usuario.builder()
                   .nome("Super Admin")
                   .email("sadm@email.com")
                   .senha(getSenhaCriptografada("1234"))
                   .tipo(TipoUsuario.SUPERADMIN)
                   .permissoes(new ArrayList<>())
                   .ativo(true)
                   .build();

           Permissao permissao = new Permissao();
           permissao.setFuncao(Funcao.ROLE_SUPERADMIN);
           superAdmin.adicionarPermissao(permissao);

           usuarioRepository.save(superAdmin);
           permissaoRepository.save(permissao);
       }
   }

   private String getSenhaCriptografada(String senha) {
       return passwordEncoder.encode(senha);
   }
}
