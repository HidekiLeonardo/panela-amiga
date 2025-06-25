package com.hideki.panela_amiga.controller;

import com.hideki.panela_amiga.dto.ReceitaDTO;
import com.hideki.panela_amiga.service.ReceitaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/receitas")
public class ReceitaController {

    private final ReceitaService receitaService;

    public ReceitaController(ReceitaService receitaService) {
        this.receitaService = receitaService;
    }

    // Adicionar Receita
    @Operation(
            summary = "Adiciona uma nova receita",
            description = "A rota cria uma nova receita e persiste os dados no banco de dados"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Receita criada com sucesso",
                    content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Erro na requisição. Dados inválidos"
            )
    })
    @PostMapping
    public ResponseEntity<String> addReceita(
            @Parameter(description = "Objeto contém os dados da receita a ser cadastrada")
            @RequestBody ReceitaDTO receita
    ) {
        ReceitaDTO receitaDTO = receitaService.addReceita(receita);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("A receita " + receitaDTO.getNome() + " foi adicionada com sucesso!");
    }

    // Mostrar Receita (ID)
    @Operation(
            summary = "Exibe uma receita",
            description = "Exibe uma receita salva no banco de dados pelo ID"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Exibe a receita pelo ID indicado pelo usuário",
                    content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "ID não existente no banco de dados"
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> mostrarReceita(
            @Parameter(description = "Atributo para localizar o objeto no banco de dados")
            @PathVariable Long id
    ) {
        ReceitaDTO receitaDTO = receitaService.mostrarReceita(id);
        if (receitaDTO != null) {
            return ResponseEntity.ok(receitaDTO);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("A receita com o id " + id + " não existe. Tente novamente...");
        }
    }

    // Mostar Receitas
    @Operation(
            summary = "Exibe todas as receitas",
            description = "Exibe todas as receitas salvas no banco de dados"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Exibe todas as receitas salvas no banco de dados",
                    content = @Content(mediaType = "application/json")
            )
    })
    @GetMapping
    public ResponseEntity<List<ReceitaDTO>> mostrarTodasReceitas() {
        List<ReceitaDTO> receitaDTOS = receitaService.mostrarTodasReceitas();
        return ResponseEntity.ok(receitaDTOS);
    }

    // Alterar Receita
    @Operation(
            summary = "Altera uma receita",
            description = "Altera uma receita indicada pelo usuário, localizada pelo id"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Receita alterada indicada pelo ID",
                    content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Receita com o ID indicado não existe no banco de dados"
            )
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> alterarReceita(
            @Parameter(description = "ID da receita")
            @PathVariable Long id,
            @Parameter(description = "Objeto contendo os dados da receita a ser alterada")
            @RequestBody ReceitaDTO receita
    ) {
        ReceitaDTO receitaDTO = receitaService.alterarReceita(id, receita);
        if (receitaDTO != null) {
            return ResponseEntity.ok(receitaDTO);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("A receita com o id " + id + " não existe. Tente novamente...");
        }
    }

    // Deletar Receita
    @Operation(
            summary = "Deleta uma receita",
            description = "Deleta uma receita cadastrada no banco de dados, localizada pelo ID"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Receita deletada do banco de dados"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "ID não encontrado no banco de dados"
            )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletarReceita(
            @Parameter(description = "ID do objeto")
            @PathVariable Long id
    ) {
        if (receitaService.mostrarReceita(id) != null) {
            receitaService.deletarReceita(id);
            return ResponseEntity.ok("A receita com o id " + id + " foi deletada com sucesso!");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("A receita com o id " + id + " não existe. Tente novamente...");
        }
    }
}
