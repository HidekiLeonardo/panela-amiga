package com.hideki.panela_amiga.model;

import com.hideki.panela_amiga.model.enums.CategoriaReceita;
import jakarta.persistence.*;
import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tb_receitas")
public class ReceitaModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private String ingredientes;

    @Column(name = "modo_preparo", nullable = false)
    private String modoPreparo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CategoriaReceita categoria;

    @Column(name = "tempo_preparo", nullable = false)
    private int tempoPreparo;

    @Column(nullable = false)
    private int porcoes;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getIngredientes() {
        return ingredientes;
    }

    public void setIngredientes(String ingredientes) {
        this.ingredientes = ingredientes;
    }

    public String getModoPreparo() {
        return modoPreparo;
    }

    public void setModoPreparo(String modoPreparo) {
        this.modoPreparo = modoPreparo;
    }

    public CategoriaReceita getCategoria() {
        return categoria;
    }

    public void setCategoria(CategoriaReceita categoria) {
        this.categoria = categoria;
    }

    public int getTempoPreparo() {
        return tempoPreparo;
    }

    public void setTempoPreparo(int tempoPreparo) {
        this.tempoPreparo = tempoPreparo;
    }

    public int getPorcoes() {
        return porcoes;
    }

    public void setPorcoes(int porcoes) {
        this.porcoes = porcoes;
    }
}
