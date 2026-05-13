package com.hideki.panela_amiga.controller;

import com.hideki.panela_amiga.dto.RelatorioFinanceiroDTO;
import com.hideki.panela_amiga.service.RelatorioFinanceiroService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/relatorio-financeiro")
public class RelatorioFinanceiroController {

    private final RelatorioFinanceiroService relatorioFinanceiroService;

    public RelatorioFinanceiroController(RelatorioFinanceiroService relatorioFinanceiroService) {
        this.relatorioFinanceiroService = relatorioFinanceiroService;
    }

    @GetMapping
    public ResponseEntity<RelatorioFinanceiroDTO> gerarRelatorio() {
        return ResponseEntity.ok(relatorioFinanceiroService.gerarRelatorio());
    }
}
