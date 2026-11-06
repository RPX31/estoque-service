package estoque_service.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import jakarta.validation.constraints.Size;

public record AjusteEstoqueRequestDTO(

        @NotNull(message = "A nova quantidade é obrigatória")
        @PositiveOrZero(message = "A nova quantidade não pode ser negativa")
        Integer novaQuantidade,

        @Size(max = 255, message = "O motivo deve ter no máximo 255 caracteres")
        String motivo

) {
}