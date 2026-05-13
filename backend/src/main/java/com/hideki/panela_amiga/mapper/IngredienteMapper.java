package com.hideki.panela_amiga.mapper;

import com.hideki.panela_amiga.dto.IngredienteDTO;
import com.hideki.panela_amiga.model.IngredienteModel;
import org.springframework.stereotype.Component;

@Component
public class IngredienteMapper {

    public IngredienteModel toModel(IngredienteDTO dto) {
        IngredienteModel ingredienteModel = new IngredienteModel();
        ingredienteModel.setId(dto.getId());
        ingredienteModel.setNome(dto.getNome());
        ingredienteModel.setUnidadeDeMedida(dto.getUnidadeDeMedida());
        ingredienteModel.setMarca(dto.getMarca());
        ingredienteModel.setFornecedor(dto.getFornecedor());
        ingredienteModel.setCustoUnitario(dto.getCustoUnitario());
        ingredienteModel.setQuantidadeEstoque(dto.getQuantidadeEstoque());
        ingredienteModel.setDataValidade(dto.getDataValidade());
        ingredienteModel.setDataAtualizacao(dto.getDataAtualizacao());
        return ingredienteModel;
    }

    public IngredienteDTO toDTO(IngredienteModel ingredienteModel) {
        IngredienteDTO dto = new IngredienteDTO();
        dto.setId(ingredienteModel.getId());
        dto.setNome(ingredienteModel.getNome());
        dto.setUnidadeDeMedida(ingredienteModel.getUnidadeDeMedida());
        dto.setMarca(ingredienteModel.getMarca());
        dto.setFornecedor(ingredienteModel.getFornecedor());
        dto.setCustoUnitario(ingredienteModel.getCustoUnitario());
        dto.setQuantidadeEstoque(ingredienteModel.getQuantidadeEstoque());
        dto.setDataValidade(ingredienteModel.getDataValidade());
        dto.setDataAtualizacao(ingredienteModel.getDataAtualizacao());
        return dto;
    }

}
