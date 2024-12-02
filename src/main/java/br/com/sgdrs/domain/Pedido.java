package br.com.sgdrs.domain;

import br.com.sgdrs.domain.enums.StatusPedido;
import jakarta.persistence.*;
import lombok.*;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.GenerationType.UUID;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Pedido {
    @Id
    @GeneratedValue(strategy = UUID)
    @Column(name = "id_pedido")
    private UUID id;

    private LocalDate data;

    @ManyToOne
    @JoinColumn(name = "id_abrigo")
    private Abrigo abrigo;

    @ManyToOne
    @JoinColumn(name = "id_centro")
    private CentroDistribuicao centroDistribuicao;

    @ManyToOne
    @JoinColumn(name = "id_voluntario")
    private Usuario voluntario;

    @Enumerated(STRING)
    private StatusPedido status;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Movimentacao> itensPedido;
}
