package br.com.sgdrs.domain;


import jakarta.persistence.*;
import lombok.*;

import static jakarta.persistence.GenerationType.UUID;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class ProdutoDoacao {

    @Id
    @GeneratedValue(strategy = UUID)
    @Column(name = "id_produto_doacao")
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "id_doacao")
    private Doacao doacao;

    @ManyToOne
    @JoinColumn(name = "id_item")
    private Item item;

    private int quantidade;
}
