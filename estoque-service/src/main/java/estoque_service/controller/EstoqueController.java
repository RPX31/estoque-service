package estoque_service.controller;

import estoque_service.dto.EstoqueRequestDTO;
import estoque_service.dto.EstoqueResponseDTO;
import estoque_service.dto.VendaDTO;
import estoque_service.service.EstoqueService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/estoques")
@RequiredArgsConstructor
public class EstoqueController {

    private final EstoqueService estoqueService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EstoqueResponseDTO criarEstoque(
            @Valid @RequestBody EstoqueRequestDTO request
    ) {
        return estoqueService.criarEstoque(request);
    }

    @GetMapping("/produto/{produtoId}")
    public EstoqueResponseDTO buscarPorProdutoId(
            @PathVariable Long produtoId
    ) {
        return estoqueService.buscarPorProdutoId(produtoId);
    }

    @PostMapping("/retirada")
    public ResponseEntity<Void> realizarRetirada(@Valid @RequestBody VendaDTO vendaDTO) {
        estoqueService.realizarRetirada(vendaDTO.produtoId(), vendaDTO.quantidade());
        return ResponseEntity.ok().build();
    }
}