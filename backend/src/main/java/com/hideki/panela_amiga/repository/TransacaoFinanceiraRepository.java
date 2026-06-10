package com.hideki.panela_amiga.repository;

import com.hideki.panela_amiga.model.TransacaoFinanceiraModel;
import com.hideki.panela_amiga.model.UsuarioModel;
import com.hideki.panela_amiga.model.enums.OrigemTransacao;
import com.hideki.panela_amiga.model.enums.TipoTransacao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TransacaoFinanceiraRepository extends JpaRepository<TransacaoFinanceiraModel, Long> {
    List<TransacaoFinanceiraModel> findByDataBetweenAndUsuario(LocalDate inicio, LocalDate fim, UsuarioModel usuario);
    List<TransacaoFinanceiraModel> findByReceitaIdAndUsuario(Long id, UsuarioModel usuario);
    List<TransacaoFinanceiraModel> findByTipoTransacaoAndUsuario(TipoTransacao tipoTransacao, UsuarioModel usuario);
    List<TransacaoFinanceiraModel> findByOrigemTransacaoAndUsuario(OrigemTransacao origemTransacao, UsuarioModel usuario);
    List<TransacaoFinanceiraModel> findAllByUsuario(UsuarioModel usuario);
    Optional<TransacaoFinanceiraModel> findByIdAndUsuario(Long id, UsuarioModel usuario);
}