package com.hideki.panela_amiga.mapper;

import com.hideki.panela_amiga.dto.TransacaoFinanceiraDTO;
import com.hideki.panela_amiga.model.TransacaoFinanceiraModel;
import com.hideki.panela_amiga.repository.ReceitaRepository;
import org.springframework.stereotype.Component;

@Component
public class TransacaoFinanceiraMapper {

    private final ReceitaRepository receitaRepository;

    public TransacaoFinanceiraMapper(ReceitaRepository receitaRepository) {
        this.receitaRepository = receitaRepository;
    }

    public TransacaoFinanceiraModel toModel(TransacaoFinanceiraDTO dto) {
        TransacaoFinanceiraModel transacaoFinanceiraModel = new TransacaoFinanceiraModel();
        transacaoFinanceiraModel.setId(dto.getId());
        transacaoFinanceiraModel.setTipoTransacao(dto.getTipoTransacao());
        transacaoFinanceiraModel.setDescricao(dto.getDescricao());
        transacaoFinanceiraModel.setValor(dto.getValor());
        transacaoFinanceiraModel.setData(dto.getData());
        transacaoFinanceiraModel.setPagamento(dto.getPagamento());
        transacaoFinanceiraModel.setOrigemTransacao(dto.getOrigemTransacao());

        if (dto.getReceitaId() != null) {
            transacaoFinanceiraModel.setReceita(receitaRepository.findById(dto.getReceitaId()).orElse(null));
        }

        return transacaoFinanceiraModel;
    }

    public TransacaoFinanceiraDTO toDTO(TransacaoFinanceiraModel transacaoFinanceiraModel){
        TransacaoFinanceiraDTO transacaoFinanceiraDTO = new TransacaoFinanceiraDTO();
        transacaoFinanceiraDTO.setId(transacaoFinanceiraModel.getId());;
        transacaoFinanceiraDTO.setTipoTransacao(transacaoFinanceiraModel.getTipoTransacao());
        transacaoFinanceiraDTO.setDescricao(transacaoFinanceiraModel.getDescricao());
        transacaoFinanceiraDTO.setValor(transacaoFinanceiraModel.getValor());
        transacaoFinanceiraDTO.setData(transacaoFinanceiraModel.getData());
        transacaoFinanceiraDTO.setPagamento(transacaoFinanceiraModel.getPagamento());
        transacaoFinanceiraDTO.setOrigemTransacao(transacaoFinanceiraModel.getOrigemTransacao());

        if (transacaoFinanceiraModel.getReceita() != null) {
            transacaoFinanceiraDTO.setReceitaId(transacaoFinanceiraModel.getReceita().getId());
        }

        return transacaoFinanceiraDTO;
    }
}
