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
@Entity
public class Item {
    @Id
    @GeneratedValue(strategy = UUID)
    @Column(name = "id_item")
    private UUID id;
    private String nome;
    private String descricao;
    private int quantidade;
    private String categoria;
    private String codBarras;
    private boolean validado;

    @OneToMany(mappedBy = "item")
    private List<ProdutoDoacao> produtosDoacao;

    @OneToMany(mappedBy = "item")
    private List<Movimentacao> itensMovimentados;

    @ManyToOne
    @JoinColumn(name = "id_centro")
    private CentroDistribuicao centroDistribuicao;
}
