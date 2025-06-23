package com.hideki.panela_amiga.controller;

import com.hideki.panela_amiga.dto.ReceitaDTO;
import com.hideki.panela_amiga.service.ReceitaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/receitas")
public class ReceitaController {

    @Autowired
    private ReceitaService receitaService;

    // Adicionar Receita
    @PostMapping
    public ResponseEntity<String> addReceita(@RequestBody ReceitaDTO receita) {
        ReceitaDTO receitaDTO = receitaService.addReceita(receita);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("A receita " + receitaDTO.getNome() + " foi adicionada com sucesso!");
    }

    // Mostrar Receita (ID)
    @GetMapping("/{id}")
    public ResponseEntity<?> mostrarReceita(@PathVariable Long id) {
        ReceitaDTO receitaDTO = receitaService.mostrarReceita(id);
        if (receitaDTO != null) {
            return ResponseEntity.ok(receitaDTO);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("A receita com o id " + id + " não existe. Tente novamente...");
        }
    }

    // Mostar Receitas
    @GetMapping
    public ResponseEntity<List<ReceitaDTO>> mostrarTodasReceitas() {
        List<ReceitaDTO> receitaDTOS = receitaService.mostrarTodasReceitas();
        return ResponseEntity.ok(receitaDTOS);
    }

    // Alterar Receita
    @PutMapping("/{id}")
    public ResponseEntity<?> alterarReceita(@PathVariable Long id, @RequestBody ReceitaDTO receita) {
        ReceitaDTO receitaDTO = receitaService.alterarReceita(id, receita);
        if (receitaDTO != null) {
            return ResponseEntity.ok(receitaDTO);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("A receita com o id " + id + " não existe. Tente novamente...");
        }
    }

    // Deletar Receita
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletarReceita(@PathVariable Long id) {
        if (receitaService.mostrarReceita(id) != null) {
            receitaService.deletarReceita(id);
            return ResponseEntity.ok("A receita com o id " + id + " foi deletada com sucesso!");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("A receita com o id " + id + " não existe. Tente novamente...");
        }
    }
}
