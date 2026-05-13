package com.hideki.panela_amiga.repository;

import com.hideki.panela_amiga.model.ReceitaModel;
import com.hideki.panela_amiga.model.enums.CategoriaReceita;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReceitaRepository extends JpaRepository<ReceitaModel, Long> {
    List<ReceitaModel> findByCategoria(CategoriaReceita categoria);
    List<ReceitaModel> findByNomeContainingIgnoreCase(String nome);
}
