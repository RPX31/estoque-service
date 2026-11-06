package estoque_service.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record MovimentacaoEstoqueRequestDTO(

        @NotNull(message = "A quantidade é obrigatória")
        @Positive(message = "A quantidade deve ser maior que zero")
        Integer quantidade,

        @Size(max = 255, message = "O motivo deve ter no máximo 255 caracteres")
        String motivo

) {
}