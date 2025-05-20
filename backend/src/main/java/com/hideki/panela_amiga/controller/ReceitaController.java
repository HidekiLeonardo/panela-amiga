package com.hideki.panela_amiga.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/receitas")
public class ReceitaController {
    // Adicionar Receita
    @PostMapping
    public String addReceita() {
        return "Receita adicionada";
    }

    // Mostrar Receita (ID)
    @GetMapping("/id")
    public String mostrarReceita() {
        return "Receita";
    }

    // Mostar Receitas
    @GetMapping
    public String mostrarTodasReceitas() {
        return "Receitas";
    }
    // Alterar Receita
    @PutMapping("/id")
    public String alterarReceita() {
        return "Receita Alterada";
    }

    // Deletar Receita
    @DeleteMapping("/id")
    public String deletarReceita() {
        return "Receita Deletada";
    }
}
