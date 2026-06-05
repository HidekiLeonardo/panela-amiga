package com.hideki.panela_amiga.model;

import com.hideki.panela_amiga.model.enums.FormaPagamento;
import com.hideki.panela_amiga.model.enums.OrigemTransacao;
import com.hideki.panela_amiga.model.enums.TipoTransacao;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "tb_transacoes_financeiras")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TransacaoFinanceiraModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, name = "tipo_transacao")
    @Enumerated(EnumType.STRING)
    private TipoTransacao tipoTransacao;

    private String descricao;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal valor;

    @Column(nullable = false)
    private LocalDate data;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private FormaPagamento pagamento;

    @Column(nullable = false, name = "origem_transacao")
    @Enumerated(EnumType.STRING)
    private OrigemTransacao origemTransacao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receita_id")
    @ToString.Exclude
    private ReceitaModel receita;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private UsuarioModel usuario;
}
