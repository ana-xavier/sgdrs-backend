package br.com.sgdrs.domain;

import jakarta.persistence.*;
import lombok.*;

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
public class Endereco {
    @Id
    @GeneratedValue(strategy = UUID)
    @Column(name = "id_endereco")
    private UUID id;
    private String cep;
    private String logradouro;
    private String numero;
    private String bairro;
    private String cidade;
    private String estado;
}
