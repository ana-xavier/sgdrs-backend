package br.com.sgdrs.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static jakarta.persistence.GenerationType.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@ToString(of = "id")
@Entity
public class Doacao {
    @Id
    @GeneratedValue(strategy = UUID)
    @Column(name = "id_doacao")
    private UUID id;

    private LocalDate data;

    @ManyToOne
    @JoinColumn(name = "id_doador")
    private Doador doador;

    @ManyToOne
    @JoinColumn(name = "id_centro")
    private CentroDistribuicao centroDistribuicao;

    @OneToMany(mappedBy = "doacao")
    private List<ProdutoDoacao> produtosDoacao;
}
