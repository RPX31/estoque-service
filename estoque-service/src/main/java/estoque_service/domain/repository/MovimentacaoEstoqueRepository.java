package estoque_service.domain.repository;

import estoque_service.domain.entity.MovimentacaoEstoque;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MovimentacaoEstoqueRepository
        extends JpaRepository<MovimentacaoEstoque, Long> {

    List<MovimentacaoEstoque> findByEstoqueIdOrderByDataMovimentacaoDesc(Long estoqueId);
}