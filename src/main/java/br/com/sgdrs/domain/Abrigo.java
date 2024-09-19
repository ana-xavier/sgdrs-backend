package br.com.sgdrs.domain;

import jakarta.persistence.*;
import lombok.*;

import static jakarta.persistence.GenerationType.UUID;
import java.util.UUID;


@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@ToString(of = "id")
public class Abrigo {
    @Id
    @GeneratedValue(strategy = UUID)
    @Column(name = "id_abrigo")
    private UUID id;

    private String nome;

    @OneToOne
    @JoinColumn(name = "id_endereco")
    private Endereco endereco;
}
