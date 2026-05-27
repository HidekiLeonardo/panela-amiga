package com.hideki.panela_amiga.model;

import com.hideki.panela_amiga.model.enums.UnidadeMedidaIngrediente;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@ToString
@Table(name = "tb_ingredientes")
public class IngredienteModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(name = "unidade_medida", nullable = false)
    @Enumerated(EnumType.STRING)
    private UnidadeMedidaIngrediente unidadeDeMedida;

    @Column(nullable = false)
    private String marca;

    private String fornecedor;

    @Column(name = "custo_unitario", precision = 10, scale = 2)
    private BigDecimal custoUnitario;

    @Column(name = "quantidade_em_estoque")
    private BigDecimal quantidadeEstoque;

    @Column(name = "data_validade", nullable = false)
    private LocalDate dataValidade;

    @Column(name = "data_atualizacao")
    @UpdateTimestamp
    private LocalDateTime dataAtualizacao;
}
