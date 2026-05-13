package com.hideki.panela_amiga.repository;

import com.hideki.panela_amiga.model.TransacaoFinanceiraModel;
import com.hideki.panela_amiga.model.enums.OrigemTransacao;
import com.hideki.panela_amiga.model.enums.TipoTransacao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface TransacaoFinanceiraRepository extends JpaRepository<TransacaoFinanceiraModel, Long> {
    List<TransacaoFinanceiraModel> findByDataBetween(LocalDate inicio, LocalDate fim);
    List<TransacaoFinanceiraModel> findByReceitaId(Long id);
    List<TransacaoFinanceiraModel> findByTipoTransacao(TipoTransacao tipoTransacao);
    List<TransacaoFinanceiraModel> findByOrigemTransacao(OrigemTransacao origemTransacao);
}