package com.hideki.panela_amiga.mapper;

import com.hideki.panela_amiga.dto.ReceitaDTO;
import com.hideki.panela_amiga.model.ReceitaModel;
import org.springframework.stereotype.Component;

@Component
public class ReceitaMapper {
    public ReceitaModel map(ReceitaDTO dto) {
        ReceitaModel receitaModel = new ReceitaModel();
        receitaModel.setId(dto.getId());
        receitaModel.setNome(dto.getNome());
        receitaModel.setIngredientes(dto.getIngredientes());
        receitaModel.setCategoria(dto.getCategoria());
        receitaModel.setModoPreparo(dto.getModoPreparo());
        receitaModel.setTempoPreparo(dto.getTempoPreparo());
        receitaModel.setPorcoes(dto.getPorcoes());
        return receitaModel;
    }

    public ReceitaDTO map(ReceitaModel receitaModel) {
        ReceitaDTO dto = new ReceitaDTO();
        dto.setId(receitaModel.getId());
        dto.setNome(receitaModel.getNome());
        dto.setIngredientes(receitaModel.getIngredientes());
        dto.setCategoria(receitaModel.getCategoria());
        dto.setModoPreparo(receitaModel.getModoPreparo());
        dto.setTempoPreparo(receitaModel.getTempoPreparo());
        dto.setPorcoes(receitaModel.getPorcoes());
        return dto;
    }
}
