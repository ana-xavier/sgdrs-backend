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
public class Movimentacao {
    @Id
    @GeneratedValue(strategy = UUID)
    @Column(name = "id_movimentacao")
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "id_item")
    private Item item;

    @ManyToOne
    @JoinColumn(name = "id_pedido")
    private Pedido pedido;

    private int quantidade;
}
