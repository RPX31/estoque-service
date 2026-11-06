
package estoque_service.service;

import estoque_service.domain.entity.Estoque;
import estoque_service.domain.repository.EstoqueRepository;
import estoque_service.dto.EstoqueRequestDTO;
import estoque_service.dto.EstoqueResponseDTO;
import estoque_service.exception.BusinessRuleException;
import estoque_service.exception.ResourceNotFoundException;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EstoqueService {

    private final EstoqueRepository estoqueRepository;

    @Transactional
    public EstoqueResponseDTO criarEstoque(EstoqueRequestDTO request) {

        if (estoqueRepository.existsByProdutoId(request.produtoId())) {
            throw new BusinessRuleException(
                    "Já existe um estoque cadastrado para este produto"
            );
        }

        Estoque estoque = new Estoque();
        estoque.setProdutoId(request.produtoId());
        estoque.setQuantidade(request.quantidade());

        Estoque estoqueSalvo = estoqueRepository.save(estoque);

        return converterParaResponse(estoqueSalvo);
    }

    @Transactional(readOnly = true)
    public EstoqueResponseDTO buscarPorProdutoId(Long produtoId) {

        Estoque estoque = estoqueRepository.findByProdutoId(produtoId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Estoque não encontrado para o produto: " + produtoId
                ));

        return converterParaResponse(estoque);
    }

    private EstoqueResponseDTO converterParaResponse(Estoque estoque) {

        return new EstoqueResponseDTO(
                estoque.getId(),
                estoque.getProdutoId(),
                estoque.getQuantidade(),
                estoque.getDataCriacao(),
                estoque.getDataAtualizacao()
        );
    }
}
