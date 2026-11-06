package estoque_service.controller;

import estoque_service.domain.enums.TipoMovimentacao;
import estoque_service.dto.AjusteEstoqueRequestDTO;
import estoque_service.dto.MovimentacaoEstoqueRequestDTO;
import estoque_service.dto.MovimentacaoEstoqueResponseDTO;
import estoque_service.service.MovimentacaoEstoqueService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/estoques/{produtoId}/movimentacoes")
@RequiredArgsConstructor
public class MovimentacaoEstoqueController {

    private final MovimentacaoEstoqueService movimentacaoService;

    @PostMapping("/entrada")
    @ResponseStatus(HttpStatus.CREATED)
    public MovimentacaoEstoqueResponseDTO registrarEntrada(
            @PathVariable Long produtoId,
            @Valid @RequestBody MovimentacaoEstoqueRequestDTO request
    ) {
        return movimentacaoService.registrarEntrada(produtoId, request);
    }

    @PostMapping("/saida")
    @ResponseStatus(HttpStatus.CREATED)
    public MovimentacaoEstoqueResponseDTO registrarSaida(
            @PathVariable Long produtoId,
            @Valid @RequestBody MovimentacaoEstoqueRequestDTO request
    ) {
        return movimentacaoService.registrarSaida(produtoId, request);
    }

    @PostMapping("/ajuste")
    @ResponseStatus(HttpStatus.CREATED)
    public MovimentacaoEstoqueResponseDTO registrarAjuste(
            @PathVariable Long produtoId,
            @Valid @RequestBody AjusteEstoqueRequestDTO request
    ) {
        return movimentacaoService.registrarAjuste(produtoId, request);
    }
    @GetMapping("/{movimentacaoId}")
    public MovimentacaoEstoqueResponseDTO buscarPorId(
            @PathVariable Long produtoId,
            @PathVariable Long movimentacaoId
    ) {
        return movimentacaoService.buscarPorId(
                produtoId,
                movimentacaoId
        );
    }

    @GetMapping
    public Page<MovimentacaoEstoqueResponseDTO> listarHistorico(
            @PathVariable Long produtoId,

            @RequestParam(required = false)
            TipoMovimentacao tipo,

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime dataInicio,

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime dataFim,

            Pageable pageable
    ) {
        return movimentacaoService.listarHistorico(
                produtoId,
                tipo,
                dataInicio,
                dataFim,
                pageable
        );
    }
}