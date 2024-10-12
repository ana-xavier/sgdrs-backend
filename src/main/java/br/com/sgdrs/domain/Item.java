package br.com.sgdrs.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

import static jakarta.persistence.GenerationType.UUID;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
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

    @OneToMany(mappedBy = "item")
    private List<ProdutoDoacao> produtosDoacao;

    @OneToMany(mappedBy = "item")
    private List<Estoque> estoques;

    @OneToMany(mappedBy = "item")
    private List<Movimentacao> itensMovimentados;
}
