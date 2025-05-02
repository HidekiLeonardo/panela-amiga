package com.hideki.panela_amiga.repository;

import com.hideki.panela_amiga.model.ReceitaModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReceitaRepository extends JpaRepository<ReceitaModel, Long> {
}
