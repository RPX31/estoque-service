package estoque_service.service;

import estoque_service.dto.MovimentacaoEstoqueRequestDTO;
import estoque_service.exception.BusinessRuleException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EstoqueServiceTest {

    @Mock
    private MovimentacaoEstoqueService movimentacaoEstoqueService;

    @InjectMocks
    private EstoqueService estoqueService;

    @Test
    @DisplayName("Deve realizar a retirada com sucesso chamando a saída da movimentação")
    void deveRealizarRetiradaComSucesso() {
        Long produtoId = 1L;
        Integer quantidadeVenda = 3;

        // Execução
        assertDoesNotThrow(() -> estoqueService.realizarRetirada(produtoId, quantidadeVenda));

        // Validação se o serviço de movimentação de saída foi chamado corretamente
        verify(movimentacaoEstoqueService, times(1))
                .registrarSaida(eq(produtoId), any(MovimentacaoEstoqueRequestDTO.class));
    }

    @Test
    @DisplayName("Deve lançar exceção quando a quantidade da venda for nula ou menor/igual a zero")
    void deveLancarExcecaoQuandoQuantidadeInvalida() {
        Long produtoId = 1L;
        Integer quantidadeInvalida = 0;

        // Execução e Validação
        BusinessRuleException exception = assertThrows(
                BusinessRuleException.class,
                () -> estoqueService.realizarRetirada(produtoId, quantidadeInvalida)
        );

        assertEquals("A quantidade da venda deve ser maior que zero.", exception.getMessage());

        // Garante que o serviço de saída NÃO foi acionado
        verify(movimentacaoEstoqueService, never()).registrarSaida(anyLong(), any());
    }

    @Test
    @DisplayName("Deve propagar BusinessRuleException quando o MovimentacaoEstoqueService lançar saldo insuficiente")
    void devePropagarExcecaoQuandoEstoqueInsuficiente() {
        Long produtoId = 1L;
        Integer quantidadeVenda = 10;

        // Simula que o serviço de movimentação lançou a regra de saldo insuficiente
        doThrow(new BusinessRuleException("Não há saldo suficiente para realizar esta saída"))
                .when(movimentacaoEstoqueService)
                .registrarSaida(eq(produtoId), any(MovimentacaoEstoqueRequestDTO.class));

        // Execução e Validação
        BusinessRuleException exception = assertThrows(
                BusinessRuleException.class,
                () -> estoqueService.realizarRetirada(produtoId, quantidadeVenda)
        );

        assertEquals("Não há saldo suficiente para realizar esta saída", exception.getMessage());
    }
}