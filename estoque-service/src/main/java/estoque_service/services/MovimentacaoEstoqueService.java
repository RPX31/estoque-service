package estoque_service.service;

import estoque_service.domain.entity.Estoque;
import estoque_service.domain.entity.MovimentacaoEstoque;
import estoque_service.domain.enums.TipoMovimentacao;
import estoque_service.domain.repository.EstoqueRepository;
import estoque_service.domain.repository.MovimentacaoEstoqueRepository;
import estoque_service.dto.AjusteEstoqueRequestDTO;
import estoque_service.dto.MovimentacaoEstoqueRequestDTO;
import estoque_service.dto.MovimentacaoEstoqueResponseDTO;
import estoque_service.exception.BusinessRuleException;
import estoque_service.exception.ResourceNotFoundException;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class MovimentacaoEstoqueService {

    private final EstoqueRepository estoqueRepository;
    private final MovimentacaoEstoqueRepository movimentacaoRepository;

    @Transactional
    public MovimentacaoEstoqueResponseDTO registrarEntrada(
            Long produtoId,
            MovimentacaoEstoqueRequestDTO request
    ) {
        Estoque estoque = buscarEstoque(produtoId);

        Integer saldoAnterior = estoque.getQuantidade();
        Integer saldoPosterior = saldoAnterior + request.quantidade();

        estoque.setQuantidade(saldoPosterior);
        estoqueRepository.save(estoque);

        MovimentacaoEstoque movimentacao = criarMovimentacao(
                estoque, TipoMovimentacao.ENTRADA, request.quantidade(),
                saldoAnterior, saldoPosterior, request.motivo()
        );

        return converterParaResponse(movimentacaoRepository.save(movimentacao));
    }

    @Transactional
    public MovimentacaoEstoqueResponseDTO registrarSaida(
            Long produtoId,
            MovimentacaoEstoqueRequestDTO request
    ) {
        Estoque estoque = buscarEstoque(produtoId);

        Integer saldoAnterior = estoque.getQuantidade();
        Integer quantidadeSaida = request.quantidade();

        if (quantidadeSaida > saldoAnterior) {
            throw new BusinessRuleException(
                    "Não há saldo suficiente para realizar esta saída"
            );
        }

        Integer saldoPosterior = saldoAnterior - quantidadeSaida;

        estoque.setQuantidade(saldoPosterior);
        estoqueRepository.save(estoque);

        MovimentacaoEstoque movimentacao = criarMovimentacao(
                estoque, TipoMovimentacao.SAIDA, quantidadeSaida,
                saldoAnterior, saldoPosterior, request.motivo()
        );

        return converterParaResponse(movimentacaoRepository.save(movimentacao));
    }

    @Transactional
    public MovimentacaoEstoqueResponseDTO registrarAjuste(
            Long produtoId,
            AjusteEstoqueRequestDTO request
    ) {
        Estoque estoque = buscarEstoque(produtoId);

        Integer saldoAnterior = estoque.getQuantidade();
        Integer novoSaldo = request.novaQuantidade();
        Integer diferenca = novoSaldo - saldoAnterior;

        estoque.setQuantidade(novoSaldo);
        estoqueRepository.save(estoque);

        MovimentacaoEstoque movimentacao = criarMovimentacao(
                estoque, TipoMovimentacao.AJUSTE, diferenca,
                saldoAnterior, novoSaldo, request.motivo()
        );

        return converterParaResponse(movimentacaoRepository.save(movimentacao));
    }

    @Transactional(readOnly = true)
    public Page<MovimentacaoEstoqueResponseDTO> listarHistorico(
            Long produtoId,
            TipoMovimentacao tipo,
            LocalDateTime dataInicio,
            LocalDateTime dataFim,
            Pageable pageable
    ) {
        Estoque estoque = buscarEstoque(produtoId);

        if ((dataInicio == null && dataFim != null)
                || (dataInicio != null && dataFim == null)) {

            throw new BusinessRuleException(
                    "Informe a data inicial e a data final para filtrar o período"
            );
        }

        if (dataInicio != null && dataInicio.isAfter(dataFim)) {
            throw new BusinessRuleException(
                    "A data inicial não pode ser posterior à data final"
            );
        }

        Page<MovimentacaoEstoque> movimentacoes;

        if (dataInicio != null && dataFim != null) {

            if (tipo == null) {
                movimentacoes = movimentacaoRepository
                        .findByEstoqueIdAndDataMovimentacaoBetween(
                                estoque.getId(), dataInicio, dataFim, pageable
                        );
            } else {
                movimentacoes = movimentacaoRepository
                        .findByEstoqueIdAndTipoAndDataMovimentacaoBetween(
                                estoque.getId(), tipo, dataInicio, dataFim, pageable
                        );
            }

        } else if (tipo != null) {

            movimentacoes = movimentacaoRepository
                    .findByEstoqueIdAndTipo(estoque.getId(), tipo, pageable);

        } else {

            movimentacoes = movimentacaoRepository
                    .findByEstoqueId(estoque.getId(), pageable);
        }

        return movimentacoes.map(this::converterParaResponse);
    }
    @Transactional(readOnly = true)
    public MovimentacaoEstoqueResponseDTO buscarPorId(
            Long produtoId,
            Long movimentacaoId
    ) {
        Estoque estoque = buscarEstoque(produtoId);

        MovimentacaoEstoque movimentacao = movimentacaoRepository
                .findByIdAndEstoqueId(movimentacaoId, estoque.getId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Movimentação não encontrada com o ID: " + movimentacaoId
                ));

        return converterParaResponse(movimentacao);
    }

    private Estoque buscarEstoque(Long produtoId) {
        return estoqueRepository.findByProdutoId(produtoId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Estoque não encontrado para o produto: " + produtoId
                ));
    }

    private MovimentacaoEstoque criarMovimentacao(
            Estoque estoque,
            TipoMovimentacao tipo,
            Integer quantidade,
            Integer saldoAnterior,
            Integer saldoPosterior,
            String motivo
    ) {
        MovimentacaoEstoque movimentacao = new MovimentacaoEstoque();
        movimentacao.setEstoque(estoque);
        movimentacao.setTipo(tipo);
        movimentacao.setQuantidade(quantidade);
        movimentacao.setSaldoAnterior(saldoAnterior);
        movimentacao.setSaldoPosterior(saldoPosterior);
        movimentacao.setMotivo(motivo);

        return movimentacao;
    }

    private MovimentacaoEstoqueResponseDTO converterParaResponse(
            MovimentacaoEstoque movimentacao
    ) {
        return new MovimentacaoEstoqueResponseDTO(
                movimentacao.getId(),
                movimentacao.getEstoque().getId(),
                movimentacao.getTipo(),
                movimentacao.getQuantidade(),
                movimentacao.getSaldoAnterior(),
                movimentacao.getSaldoPosterior(),
                movimentacao.getMotivo(),
                movimentacao.getDataMovimentacao()
        );
    }
}