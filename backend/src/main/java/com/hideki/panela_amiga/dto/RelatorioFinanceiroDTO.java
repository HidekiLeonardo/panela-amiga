package com.hideki.panela_amiga.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RelatorioFinanceiroDTO {

    private BigDecimal totalEntradas;
    private BigDecimal totalSaida;
    private BigDecimal lucroLiquido;
    private BigDecimal ticketMedio;
    private Long quantidadeVendas;

}
