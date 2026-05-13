package com.hideki.panela_amiga.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IngredienteReceitaDTO {

    private Long ingredienteId;
    private BigDecimal quantidade;
}
