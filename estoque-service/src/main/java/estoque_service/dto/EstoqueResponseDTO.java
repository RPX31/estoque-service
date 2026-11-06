package estoque_service.dto;

import java.time.LocalDateTime;

public record EstoqueResponseDTO(

        Long id,
        Long produtoId,
        Integer quantidade,
        LocalDateTime dataCriacao,
        LocalDateTime dataAtualizacao

) {
}