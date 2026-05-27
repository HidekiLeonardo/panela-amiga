package com.hideki.panela_amiga.dto;

import com.hideki.panela_amiga.model.enums.UnidadeMedidaIngrediente;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IngredienteDTO {

    private Long id;
    private String nome;
    private UnidadeMedidaIngrediente unidadeDeMedida;
    private String marca;
    private String fornecedor;
    private BigDecimal custoUnitario;
    private BigDecimal quantidadeEstoque;
    private LocalDate dataValidade;
    private LocalDateTime dataAtualizacao;

}
