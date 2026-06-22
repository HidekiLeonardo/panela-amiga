package com.hideki.panela_amiga.model;

import com.hideki.panela_amiga.model.enums.TipoMovimentacao;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "tb_movimentacao_estoque")
public class MovimentacaoEstoque {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private IngredienteModel ingrediente;

    @Enumerated(EnumType.STRING)
    private TipoMovimentacao tipo;

    private BigDecimal quantidade;

    private LocalDateTime data;

    @ManyToOne
    private UsuarioModel usuario;
}
