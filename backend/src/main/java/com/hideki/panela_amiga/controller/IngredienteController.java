package com.hideki.panela_amiga.controller;

import com.hideki.panela_amiga.dto.IngredienteDTO;
import com.hideki.panela_amiga.service.IngredienteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ingredientes")
public class IngredienteController {

    private final IngredienteService ingredienteService;

    public IngredienteController(IngredienteService ingredienteService) {
        this.ingredienteService = ingredienteService;
    }

    // Adicionar Ingrediente
    @PostMapping
    public ResponseEntity<String> addIngrediente(
            @RequestBody IngredienteDTO ingrediente
    ) {
        IngredienteDTO ingredienteDTO = ingredienteService.addIngrediente(ingrediente);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("O ingrediente " + ingredienteDTO.getNome() + " foi adicionado com sucesso!");
    }

    // Mostrar Ingrediente (ID)
    @GetMapping("/{id}")
    public ResponseEntity<?> mostrarIngrediente(
            @PathVariable Long id
    ) {
        IngredienteDTO ingredienteDTO = ingredienteService.mostrarIngrediente(id);
        if (ingredienteDTO != null) {
            return ResponseEntity.ok(ingredienteDTO);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("O ingrediente com o id " + id + " não existe. Tente novamente...");
        }
    }

    // Mostrar Ingredientes (Nome)

    // Mostrar Ingredientes (Marca)

    // Mostrar Ingredientes (Fornecedor)

    // Mostrar Ingredientes (Próximo da validade)

    // Mostrar Ingredientes (Sem estoque)

    // Atualizar Estoque

    // Mostrar Ingredientes
    @GetMapping
    public ResponseEntity<List<IngredienteDTO>> mostrarTodosIngredientes() {
        List<IngredienteDTO> ingredienteDTOS = ingredienteService.mostrarTodosIngredientes();
        return ResponseEntity.ok(ingredienteDTOS);
    }

    // Alterar Ingrediente
    @PutMapping("/{id}")
    public ResponseEntity<?> alterarIngrediente (
            @PathVariable Long id,
            @RequestBody IngredienteDTO ingrediente
    ) {
        IngredienteDTO ingredienteDTO = ingredienteService.alterarIngrediente(id, ingrediente);
        if (ingredienteDTO != null) {
            return ResponseEntity.ok(ingredienteDTO);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("O ingrediente com o id " + id + " não existe. Tente novamente...");
        }
    }

    // Deletar Ingrediente
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletarIngrediente(
            @PathVariable Long id
    ) {
        if (ingredienteService.mostrarIngrediente(id) != null) {
            ingredienteService.deletarIngrediente(id);
            return ResponseEntity.ok("O ingrediente com o id " + id + " foi deletado com sucesso!");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("O ingrediente com o id " + id + " não existe. Tente novamente...");
        }
    }
}
