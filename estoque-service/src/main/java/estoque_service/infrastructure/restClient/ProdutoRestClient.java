package estoque_service.infrastructure.restClient;

import estoque_service.dto.ProdutoResponseDTO;
import estoque_service.exception.ExternalServiceException;
import estoque_service.exception.ResourceNotFoundException;

import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

@Component
public class ProdutoRestClient {

    private final RestClient restClient;

    public ProdutoRestClient() {
        this.restClient = RestClient.builder()
                .baseUrl("http://localhost:8082")
                .build();
    }

    public ProdutoResponseDTO buscarPorId(Long produtoId) {

        try {
            return restClient.get()
                    .uri("/produtos/{id}", produtoId)
                    .retrieve()
                    .body(ProdutoResponseDTO.class);

        } catch (HttpClientErrorException.NotFound exception) {

            throw new ResourceNotFoundException(
                    "Produto não encontrado com o ID: " + produtoId
            );

        } catch (RestClientException exception) {

            throw new ExternalServiceException(
                    "Não foi possível conectar ao Produto Service no momento"
            );
        }
    }
}