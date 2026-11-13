package estoque_service.service;

import estoque_service.domain.entity.Estoque;
import estoque_service.domain.repository.EstoqueRepository;
import estoque_service.dto.EstoqueRequestDTO;
import estoque_service.dto.EstoqueResponseDTO;
import estoque_service.dto.MovimentacaoEstoqueRequestDTO;
import estoque_service.exception.BusinessRuleException;
import estoque_service.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EstoqueService {

    private final EstoqueRepository estoqueRepository;
    private final MovimentacaoEstoqueService movimentacaoEstoqueService;

    public EstoqueResponseDTO buscarPorProdutoId(Long produtoId) {
        Estoque estoque = estoqueRepository.findByProdutoId(produtoId)
                .orElseThrow(() -> new ResourceNotFoundException("Estoque não encontrado para o produto ID: " + produtoId));

        return new EstoqueResponseDTO(
                estoque.getId(),
                estoque.getProdutoId(),
                estoque.getQuantidade(),
                estoque.getDataCriacao(),
                estoque.getDataAtualizacao()
        );
    }

    @Transactional
    public void realizarRetirada(Long produtoId, Integer quantidadeVenda) {
        if (quantidadeVenda == null || quantidadeVenda <= 0) {
            throw new BusinessRuleException("A quantidade da venda deve ser maior que zero.");
        }

        // Reaproveita o serviço de movimentação que já valida saldo e gera o histórico de SAIDA
        MovimentacaoEstoqueRequestDTO request = new MovimentacaoEstoqueRequestDTO(quantidadeVenda, "Retirada por venda realizada");
        movimentacaoEstoqueService.registrarSaida(produtoId, request);
    }

    @Transactional
    public EstoqueResponseDTO criarEstoque(EstoqueRequestDTO request) {
        if (estoqueRepository.existsByProdutoId(request.produtoId())) {
            throw new BusinessRuleException("Já existe um registro de estoque para o produto ID: " + request.produtoId());
        }

        // 1. Cria o registro base do estoque
        Estoque estoque = new Estoque();
        estoque.setProdutoId(request.produtoId());
        estoque.setQuantidade(0); // Começa com zero para a entrada inicial somar corretamente
        Estoque salvo = estoqueRepository.save(estoque);

        // 2. Registra a ENTRADA inicial usando o serviço de movimentação existente
        MovimentacaoEstoqueRequestDTO movimentacaoRequest = new MovimentacaoEstoqueRequestDTO(
                request.quantidade(),
                "Estoque inicial cadastrado com o produto"
        );
        movimentacaoEstoqueService.registrarEntrada(request.produtoId(), movimentacaoRequest);

        // Busca o estoque atualizado com a quantidade correta
        Estoque estoqueAtualizado = estoqueRepository.findByProdutoId(request.produtoId()).get();

        return new EstoqueResponseDTO(
                estoqueAtualizado.getId(),
                estoqueAtualizado.getProdutoId(),
                estoqueAtualizado.getQuantidade(),
                estoqueAtualizado.getDataCriacao(),
                estoqueAtualizado.getDataAtualizacao()
        );
    }
}