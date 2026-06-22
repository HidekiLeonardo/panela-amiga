package com.hideki.panela_amiga.dto;

import com.hideki.panela_amiga.model.enums.FormaPagamento;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EntradaEstoqueDTO {
    private Long ingredienteId;
    private BigDecimal quantidade;
    private BigDecimal custoUnitario;
    private FormaPagamento pagamento;
}
