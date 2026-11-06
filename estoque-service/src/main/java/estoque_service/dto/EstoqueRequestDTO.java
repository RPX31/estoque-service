package estoque_service.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record EstoqueRequestDTO(

        @NotNull(message = "O ID do produto é obrigatório")
        Long produtoId,

        @NotNull(message = "A quantidade inicial é obrigatória")
        @PositiveOrZero(message = "A quantidade inicial não pode ser negativa")
        Integer quantidade

) {
}