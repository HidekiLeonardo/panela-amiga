package com.hideki.panela_amiga.model;

import com.hideki.panela_amiga.model.enums.CategoriaReceita;
import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "tb_receitas")
public class ReceitaModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    private String ingredientes;

    @Column(name = "modo_preparo")
    private String modoPreparo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CategoriaReceita categoria;

    @Column(name = "tempo_preparo")
    private int tempoPreparo;

    private int porcoes;

}
