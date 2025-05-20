package com.hideki.panela_amiga.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

public class ReceitaController {
    // Adicionar Receita
    @PostMapping("/adicionar")
    public String addReceita() {
        return "Receita adicionada";
    }

    // Mostrar Receita (ID)
    @GetMapping("/mostrar/id")
    public String mostrarReceita() {
        return "Receita";
    }

    // Mostar Receitas
    @GetMapping("/mostrar")
    public String mostrarTodasReceitas() {
        return "Receitas";
    }
    // Alterar Receita
    @PutMapping("/mostrar")
    public String alterarReceita() {
        return "Receita Alterada";
    }

    // Deletar Receita
    @DeleteMapping("/deletar/id")
    public String deletarReceita() {
        return "Receita Deletada";
    }
}
