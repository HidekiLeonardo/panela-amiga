package com.hideki.panela_amiga.dto;

import com.hideki.panela_amiga.model.enums.FormaPagamento;
import com.hideki.panela_amiga.model.enums.OrigemTransacao;
import com.hideki.panela_amiga.model.enums.TipoTransacao;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TransacaoFinanceiraDTO {

    private Long id;
    private TipoTransacao tipoTransacao;
    private String descricao;
    private BigDecimal valor;
    private LocalDate data;
    private FormaPagamento pagamento;
    private OrigemTransacao origemTransacao;
    private Long receitaId;

}
