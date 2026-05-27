package com.hideki.panela_amiga.service;

import com.hideki.panela_amiga.dto.RelatorioFinanceiroDTO;
import com.hideki.panela_amiga.dto.TransacaoFinanceiraDTO;
import com.hideki.panela_amiga.mapper.TransacaoFinanceiraMapper;
import com.hideki.panela_amiga.model.enums.TipoTransacao;
import com.hideki.panela_amiga.repository.TransacaoFinanceiraRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

@Service
public class RelatorioFinanceiroService {

    private final TransacaoFinanceiraRepository transacaoFinanceiraRepository;
    private final TransacaoFinanceiraMapper transacaoFinanceiraMapper;

    public RelatorioFinanceiroService(TransacaoFinanceiraRepository transacaoFinanceiraRepository, TransacaoFinanceiraMapper transacaoFinanceiraMapper) {
        this.transacaoFinanceiraRepository = transacaoFinanceiraRepository;
        this.transacaoFinanceiraMapper = transacaoFinanceiraMapper;
    }

    public RelatorioFinanceiroDTO gerarRelatorio(
            LocalDate dataInicio,
            LocalDate dataFim
    ) {
        List<TransacaoFinanceiraDTO> transacaoFinanceira;

        if (dataInicio != null  && dataFim != null){
            transacaoFinanceira = transacaoFinanceiraRepository.findByDataBetween(dataInicio, dataFim)
                    .stream()
                    .map(transacaoFinanceiraMapper::toDTO)
                    .toList();
        } else {
            transacaoFinanceira = transacaoFinanceiraRepository.findAll()
                    .stream()
                    .map(transacaoFinanceiraMapper::toDTO)
                    .toList();
        }

        BigDecimal totalEntradas = transacaoFinanceira.stream()
                .filter(t -> t.getTipoTransacao() == TipoTransacao.ENTRADA)
                .map(TransacaoFinanceiraDTO::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalSaidas = transacaoFinanceira.stream()
                .filter(t -> t.getTipoTransacao() == TipoTransacao.SAIDA)
                .map(TransacaoFinanceiraDTO::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Long quantidadeVendas = transacaoFinanceira.stream()
                .filter(t -> t.getTipoTransacao() == TipoTransacao.ENTRADA)
                .count();

        BigDecimal lucro = totalEntradas.subtract(totalSaidas);

        BigDecimal ticketMedio = quantidadeVendas > 0
                ? totalEntradas.divide(BigDecimal.valueOf(quantidadeVendas), 2, RoundingMode.HALF_UP)
                : BigDecimal.ZERO;

        return new RelatorioFinanceiroDTO(
                totalEntradas,
                totalSaidas,
                lucro,
                ticketMedio,
                quantidadeVendas
        );
    }
}
