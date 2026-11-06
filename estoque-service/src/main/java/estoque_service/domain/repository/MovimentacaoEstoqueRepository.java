package estoque_service.domain.repository;

import estoque_service.domain.entity.MovimentacaoEstoque;
import estoque_service.domain.enums.TipoMovimentacao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface MovimentacaoEstoqueRepository
        extends JpaRepository<MovimentacaoEstoque, Long> {

    Page<MovimentacaoEstoque> findByEstoqueId(
            Long estoqueId,
            Pageable pageable
    );

    Page<MovimentacaoEstoque> findByEstoqueIdAndTipo(
            Long estoqueId,
            TipoMovimentacao tipo,
            Pageable pageable
    );

    Page<MovimentacaoEstoque> findByEstoqueIdAndDataMovimentacaoBetween(
            Long estoqueId,
            LocalDateTime dataInicio,
            LocalDateTime dataFim,
            Pageable pageable
    );

    Page<MovimentacaoEstoque> findByEstoqueIdAndTipoAndDataMovimentacaoBetween(
            Long estoqueId,
            TipoMovimentacao tipo,
            LocalDateTime dataInicio,
            LocalDateTime dataFim,
            Pageable pageable
    );

    Optional<MovimentacaoEstoque> findByIdAndEstoqueId(
            Long id,
            Long estoqueId
    );
}