package com.hideki.panela_amiga.model;

import com.hideki.panela_amiga.model.enums.CategoriaReceita;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "tb_receitas")
public class ReceitaModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    private String ingredientes;

    private String modoPreparo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CategoriaReceita categoria;

    private int tempoPreparo;

    private int porcoes;

}
