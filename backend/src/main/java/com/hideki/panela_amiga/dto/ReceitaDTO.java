package com.hideki.panela_amiga.dto;

import com.hideki.panela_amiga.model.enums.CategoriaReceita;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ReceitaDTO {

    private Long id;
    private String nome;
    private List<IngredienteReceitaDTO> ingredientes;
    private String modoPreparo;
    private CategoriaReceita categoria;
    private int tempoPreparo;
    private int porcoes;
    private boolean ativo = true;
    private LocalDateTime dataCriacao;
    private Double rendimento;
    private BigDecimal custoTotal;
    private BigDecimal precoVenda;

}
