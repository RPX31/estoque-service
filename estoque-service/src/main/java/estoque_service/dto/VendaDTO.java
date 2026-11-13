package estoque_service.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record VendaDTO(
        @NotNull(message = "O ID do produto é obrigatório")
        Long produtoId,

        @NotNull(message = "A quantidade da venda é obrigatória")
        @Positive(message = "A quantidade deve ser maior que zero")
        Integer quantidade
) {
}