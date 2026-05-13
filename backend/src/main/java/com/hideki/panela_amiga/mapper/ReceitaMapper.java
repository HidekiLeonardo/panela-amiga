package com.hideki.panela_amiga.mapper;

import com.hideki.panela_amiga.dto.ReceitaDTO;
import com.hideki.panela_amiga.model.ReceitaModel;
import org.springframework.stereotype.Component;

@Component
public class ReceitaMapper {

    private final IngredienteReceitaMapper ingredienteReceitaMapper;

    public ReceitaMapper(IngredienteReceitaMapper ingredienteReceitaMapper) {
        this.ingredienteReceitaMapper = ingredienteReceitaMapper;
    }

    public ReceitaModel toModel(ReceitaDTO dto) {
        ReceitaModel receitaModel = new ReceitaModel();
        receitaModel.setId(dto.getId());
        receitaModel.setNome(dto.getNome());
        receitaModel.setModoPreparo(dto.getModoPreparo());
        receitaModel.setCategoria(dto.getCategoria());
        receitaModel.setTempoPreparo(dto.getTempoPreparo());
        receitaModel.setPorcoes(dto.getPorcoes());
        receitaModel.setAtivo(dto.isAtivo());
        receitaModel.setDataCriacao(dto.getDataCriacao());
        receitaModel.setRendimento(dto.getRendimento());
        receitaModel.setCustoTotal(dto.getCustoTotal());
        receitaModel.setPrecoVenda(dto.getPrecoVenda());

        if (dto.getIngredientes() != null) {
            var ingredientes =
                    dto.getIngredientes()
                            .stream()
                            .map(item ->
                                    ingredienteReceitaMapper
                                            .toModel(item, receitaModel))
                                    .toList();

            receitaModel.setIngredientes(ingredientes);
        }

        return receitaModel;
    }

    public ReceitaDTO toDTO(ReceitaModel receitaModel) {
        ReceitaDTO dto = new ReceitaDTO();
        dto.setId(receitaModel.getId());
        dto.setNome(receitaModel.getNome());
        dto.setModoPreparo(receitaModel.getModoPreparo());
        dto.setCategoria(receitaModel.getCategoria());
        dto.setTempoPreparo(receitaModel.getTempoPreparo());
        dto.setPorcoes(receitaModel.getPorcoes());
        dto.setAtivo(receitaModel.isAtivo());
        dto.setDataCriacao(receitaModel.getDataCriacao());
        dto.setRendimento(receitaModel.getRendimento());
        dto.setCustoTotal(receitaModel.getCustoTotal());
        dto.setPrecoVenda(receitaModel.getPrecoVenda());

        if (receitaModel.getIngredientes() != null) {
            var ingredientes =
                    receitaModel.getIngredientes()
                            .stream()
                            .map(ingredienteReceitaMapper::toDTO)
                            .toList();
            dto.setIngredientes(ingredientes);
        }

        return dto;
    }
}
