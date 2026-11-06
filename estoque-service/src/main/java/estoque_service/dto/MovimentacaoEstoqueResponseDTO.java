package estoque_service.dto;

import estoque_service.domain.enums.TipoMovimentacao;

import java.time.LocalDateTime;

public record MovimentacaoEstoqueResponseDTO(

        Long id,
        Long estoqueId,
        TipoMovimentacao tipo,
        Integer quantidade,
        Integer saldoAnterior,
        Integer saldoPosterior,
        String motivo,
        LocalDateTime dataMovimentacao

) {
}