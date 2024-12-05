package br.com.sgdrs.domain;

import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
public class UsuarioSecurity implements UserDetails {
    private static final Logger log = LoggerFactory.getLogger(UsuarioSecurity.class);

    private final UUID id;
    private final String username;
    private final String password;
    private final List<SimpleGrantedAuthority> authorities;

    private final boolean accountNonExpired;
    private final boolean accountNonLocked;
    private final boolean credentialsNonExpired;
    private final boolean enabled;

    public UsuarioSecurity(Usuario usuario) {

        this.id = usuario.getId();
        this.username = usuario.getEmail();
        this.password = usuario.getSenha();

        this.accountNonExpired = usuario.isAtivo();
        this.accountNonLocked = usuario.isAtivo();
        this.credentialsNonExpired = usuario.isAtivo();
        this.enabled = usuario.isAtivo();

        this.authorities = usuario.getPermissoes().stream()
                .map(permissao -> new SimpleGrantedAuthority(permissao.getFuncao().toString()))
                .collect(Collectors.toList());
        authorities.forEach(a -> log.info("AUTORITY = {}", a.getAuthority()));
    }
}