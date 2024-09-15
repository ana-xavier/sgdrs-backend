package br.com.sgdrs.service;


import br.com.sgdrs.controller.response.UsuarioResponse;
import br.com.sgdrs.domain.UsuarioSecurity;
import br.com.sgdrs.domain.Usuario;
import br.com.sgdrs.repository.UsuarioRepository;
import br.com.sgdrs.mapper.UsuarioMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class BuscarUsuarioSecurityService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private UsuarioAutenticadoService usuarioAutenticadoService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserDetails user = usuarioRepository.findByEmail(email)
                .map(UsuarioSecurity::new)
                .orElseThrow(() -> new UsernameNotFoundException("Credenciais inválidas"));


        return user;
    }

    public UsuarioResponse buscar() {
        Usuario usuarioAutenticado = usuarioAutenticadoService.get();
        return UsuarioMapper.toResponse(usuarioAutenticado);
    }
}

