package br.com.pucrs.sgdrs.domain;

import br.com.pucrs.sgdrs.domain.enums.TipoUsuario;
import jakarta.persistence.*;
import lombok.*;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.GenerationType.UUID;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id") @ToString(of = "id")
@Entity
public class Usuario {

    @Id
    @GeneratedValue(strategy = UUID)
    private UUID id;
    private String nome;
    private String email;
    private String senha;
    private boolean ativo;

    @Enumerated(STRING)
    private TipoUsuario tipo;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Permissao> permissoes;

    public void adicionarPermissao(Permissao permissao) {
        this.permissoes.add(permissao);
        permissao.setUsuario(this);
    }
}
