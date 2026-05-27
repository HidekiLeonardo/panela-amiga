package com.hideki.panela_amiga.controller;

import com.hideki.panela_amiga.dto.TransacaoFinanceiraDTO;
import com.hideki.panela_amiga.service.TransacaoFinanceiraService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transacao")
public class TransacaoFinanceiraController {

    private final TransacaoFinanceiraService transacaoFinanceiraService;

    public TransacaoFinanceiraController(TransacaoFinanceiraService transacaoFinanceiraRepository) {
        this.transacaoFinanceiraService = transacaoFinanceiraRepository;
    }

    // Adicionar Transação Financeira
    @PostMapping
    public ResponseEntity<String> addTransacaoFinanceira(
            @RequestBody TransacaoFinanceiraDTO transacaoFinanceira
            ) {
        TransacaoFinanceiraDTO transacaoFinanceiraDTO = transacaoFinanceiraService.addTransacao(transacaoFinanceira);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("A transação financeira " + transacaoFinanceiraDTO.getId() + " foi adicionada com sucesso!");
    }

    // Mostrar Transação Financeira (ID)
    @GetMapping("/{id}")
    public ResponseEntity<?> mostrarTransacaoFinanceira(
            @PathVariable Long id
    ) {
        TransacaoFinanceiraDTO transacaoFinanceiraDTO = transacaoFinanceiraService.mostrarTransacaoID(id);
        return ResponseEntity.ok(transacaoFinanceiraDTO);
    }

    // Mostrar Transações Financeiras
    @GetMapping
    public ResponseEntity<List<TransacaoFinanceiraDTO>> mostrarTodasTransacoes() {
        List<TransacaoFinanceiraDTO> transacoesFinanceiras = transacaoFinanceiraService.mostrarTodasTransacoes();
        return ResponseEntity.ok(transacoesFinanceiras);
    }
}