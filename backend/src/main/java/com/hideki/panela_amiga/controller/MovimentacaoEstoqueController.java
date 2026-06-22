package com.hideki.panela_amiga.controller;

import com.hideki.panela_amiga.dto.EntradaEstoqueDTO;
import com.hideki.panela_amiga.service.MovimentacaoEstoqueService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/movimentacoes-estoque")
public class MovimentacaoEstoqueController {
    private final MovimentacaoEstoqueService movimentacaoEstoqueService;

    public MovimentacaoEstoqueController(MovimentacaoEstoqueService movimentacaoEstoqueService) {
        this.movimentacaoEstoqueService = movimentacaoEstoqueService;
    }

    @PostMapping
    public ResponseEntity<Void> registrarCompra(@RequestBody EntradaEstoqueDTO entradaEstoqueDTO) {
        movimentacaoEstoqueService.registrarCompra(entradaEstoqueDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
