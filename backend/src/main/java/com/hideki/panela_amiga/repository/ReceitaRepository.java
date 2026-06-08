package com.hideki.panela_amiga.repository;

import com.hideki.panela_amiga.model.ReceitaModel;
import com.hideki.panela_amiga.model.UsuarioModel;
import com.hideki.panela_amiga.model.enums.CategoriaReceita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ReceitaRepository extends JpaRepository<ReceitaModel, Long> {
    List<ReceitaModel> findAllByUsuario(UsuarioModel usuario);
    List<ReceitaModel> findByNomeContainingIgnoreCaseAndUsuario(String nome, UsuarioModel usuario);
    List<ReceitaModel> findByCategoriaAndUsuario(CategoriaReceita categoria, UsuarioModel usuario);
    Optional<ReceitaModel> findByIdAndUsuario(Long id, UsuarioModel usuario);

    @Query("SELECT CASE WHEN COUNT(r) > 0 THEN true ELSE false END " +
            "FROM ReceitaModel r JOIN r.ingredientes ir JOIN ir.ingrediente i " +
            "WHERE r.ativo = true AND i.id = :ingredienteId")
    boolean existeReceitaAtivaComIngrediente(@Param("ingredienteId") Long ingredienteId);
}
