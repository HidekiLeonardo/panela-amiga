package com.hideki.panela_amiga.repository;

import com.hideki.panela_amiga.model.MovimentacaoEstoque;
import com.hideki.panela_amiga.model.UsuarioModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MovimentacaoEstoqueRepository extends JpaRepository<MovimentacaoEstoque, Long> {
    List<MovimentacaoEstoque> findAllByUsuario(UsuarioModel usuario);
}
