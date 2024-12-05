package br.com.sgdrs.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

import static jakarta.persistence.GenerationType.UUID;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@ToString(of = "id")
@Entity
public class Doador {
    @Id
    @GeneratedValue(strategy = UUID)
    @Column(name = "id_doador")
    private UUID id;

    private String cpfCnpj;
    private String nome;

    @OneToMany(mappedBy = "doador")
    private List<Doacao> doacoes;
}
