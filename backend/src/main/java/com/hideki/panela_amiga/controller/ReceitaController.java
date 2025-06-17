package com.hideki.panela_amiga.controller;

import com.hideki.panela_amiga.dto.ReceitaDTO;
import com.hideki.panela_amiga.model.ReceitaModel;
import com.hideki.panela_amiga.service.ReceitaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/receitas")
public class ReceitaController {

    @Autowired
    private ReceitaService receitaService;

    // Adicionar Receita
    @PostMapping
    public ReceitaDTO addReceita(@RequestBody ReceitaDTO receita) {
        return receitaService.addReceita(receita);
    }

    // Mostrar Receita (ID)
    @GetMapping("/{id}")
    public ReceitaDTO mostrarReceita(@PathVariable Long id) {
        return receitaService.mostrarReceita(id);
    }

    // Mostar Receitas
    @GetMapping
    public List<ReceitaDTO> mostrarTodasReceitas() {
        return receitaService.mostrarTodasReceitas();
    }

    // Alterar Receita
    @PutMapping("/{id}")
    public ReceitaDTO alterarReceita(@PathVariable Long id, @RequestBody ReceitaDTO receita) {
        return receitaService.alterarReceita(id, receita);
    }

    // Deletar Receita
    @DeleteMapping("/{id}")
    public void deletarReceita(@PathVariable Long id) {
        receitaService.deletarReceita(id);
    }
}
