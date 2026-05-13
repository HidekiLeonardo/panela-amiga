package com.hideki.panela_amiga.mapper;

import com.hideki.panela_amiga.dto.IngredienteReceitaDTO;
import com.hideki.panela_amiga.model.IngredienteReceita;
import com.hideki.panela_amiga.model.IngredienteModel;
import com.hideki.panela_amiga.model.ReceitaModel;
import com.hideki.panela_amiga.repository.IngredienteRepository;
import org.springframework.stereotype.Component;

@Component
public class IngredienteReceitaMapper {

    private final IngredienteRepository ingredienteRepository;

    public IngredienteReceitaMapper(IngredienteRepository ingredienteRepository) {
        this.ingredienteRepository = ingredienteRepository;
    }

    public IngredienteReceita toModel(IngredienteReceitaDTO dto, ReceitaModel receita) {
        IngredienteModel ingrediente = ingredienteRepository.findById(dto.getIngredienteId())
                .orElseThrow(() -> new RuntimeException("Ingrediente não encontrado."));

        IngredienteReceita ir = new IngredienteReceita();
        ir.setReceita(receita);
        ir.setIngrediente(ingrediente);
        ir.setQuantidade(dto.getQuantidade());

        return ir;
    }

    public IngredienteReceitaDTO toDTO (IngredienteReceita model) {
        IngredienteReceitaDTO dto = new IngredienteReceitaDTO();
        dto.setIngredienteId(model.getIngrediente().getId());
        dto.setQuantidade(model.getQuantidade());

        return dto;
    }
}
