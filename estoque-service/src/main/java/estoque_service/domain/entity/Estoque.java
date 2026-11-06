
package estoque_service.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "tb_estoque",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_estoque_produto",
                        columnNames = "produto_id"
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
public class Estoque {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "produto_id", nullable = false)
    private Long produtoId;

    @Column(nullable = false)
    private Integer quantidade;

    @Column(name = "data_criacao", nullable = false)
    private LocalDateTime dataCriacao;

    @Column(name = "data_atualizacao", nullable = false)
    private LocalDateTime dataAtualizacao;

    @Version
    private Long versao;

    @PrePersist
    protected void prePersist() {
        LocalDateTime agora = LocalDateTime.now();

        this.dataCriacao = agora;
        this.dataAtualizacao = agora;
    }

    @PreUpdate
    protected void preUpdate() {
        this.dataAtualizacao = LocalDateTime.now();
    }
}
