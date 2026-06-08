package com.hideki.panela_amiga.repository;

import com.hideki.panela_amiga.model.IngredienteModel;
import com.hideki.panela_amiga.model.UsuarioModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface IngredienteRepository extends JpaRepository<IngredienteModel, Long> {
    List<IngredienteModel> findByNomeContainingIgnoreCase(String nome);
    List<IngredienteModel> findByMarca(String marca);
    List<IngredienteModel> findByFornecedor(String fornecedor);
    List<IngredienteModel> findAllByUsuario(UsuarioModel usuario);
    Optional<IngredienteModel> findByIdAndUsuario(Long id, UsuarioModel usuario);
}
