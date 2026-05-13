package com.hideki.panela_amiga.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Data
public class IngredienteReceita {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private ReceitaModel receita;

    @ManyToOne
    private IngredienteModel ingrediente;

    private BigDecimal quantidade;
}
