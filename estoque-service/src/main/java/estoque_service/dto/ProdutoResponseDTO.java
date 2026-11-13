package estoque_service.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ProdutoResponseDTO(
        Long id,
        String nome
) {
}