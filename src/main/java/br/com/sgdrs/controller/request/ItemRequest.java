package br.com.sgdrs.controller.request;

import java.util.UUID;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemRequest {

    @NotNull(message = "O id não pode ser nulo")
    private UUID id;

    @NotNull(message = "A quantidade não pode ser nula.")
    @Min(value = 1, message = "A quantidade deve ser maior que zero.")
    private Integer quantidade;
}
