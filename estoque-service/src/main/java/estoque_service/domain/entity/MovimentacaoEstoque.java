
package estoque_service.domain.entity;

import estoque_service.domain.enums.TipoMovimentacao;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "tb_movimentacao_estoque")
@Getter
@Setter
@NoArgsConstructor
public class MovimentacaoEstoque {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "estoque_id", nullable = false)
    private Estoque estoque;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoMovimentacao tipo;

    @Column(nullable = false)
    private Integer quantidade;

    @Column(name = "saldo_anterior", nullable = false)
    private Integer saldoAnterior;

    @Column(name = "saldo_posterior", nullable = false)
    private Integer saldoPosterior;

    @Column(length = 255)
    private String motivo;

    @Column(name = "data_movimentacao", nullable = false)
    private LocalDateTime dataMovimentacao;

    @PrePersist
    protected void prePersist() {
        this.dataMovimentacao = LocalDateTime.now();
    }
}
