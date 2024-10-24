package br.com.sgdrs.domain;

import jakarta.persistence.*;
import lombok.*;

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
public class Estoque {
    @Id
    @GeneratedValue(strategy = UUID)
    @Column(name = "id_estoque")
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "id_item")
    private Item item;

    @ManyToOne
    @JoinColumn(name = "id_centro")
    private CentroDistribuicao centroDistribuicao;

    private int quantidade;
}
