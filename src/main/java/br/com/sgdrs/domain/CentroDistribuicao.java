package br.com.sgdrs.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@ToString(of = "id")
@Entity
public class CentroDistribuicao {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_centro")
    private UUID id;
    @Column(name = "nome")
    private String nome;
    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "id_endereco")
    private Endereco endereco;
}
