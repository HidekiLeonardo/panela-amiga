package com.hideki.panela_amiga.service;

import com.hideki.panela_amiga.dto.ReceitaDTO;
import com.hideki.panela_amiga.mapper.ReceitaMapper;
import com.hideki.panela_amiga.model.ReceitaModel;
import com.hideki.panela_amiga.repository.ReceitaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReceitaService {

    @Autowired
    private ReceitaRepository receitaRepository;
    @Autowired
    private ReceitaMapper receitaMapper;

    // Adicionar Receita
    public ReceitaDTO addReceita(ReceitaDTO receitaDTO) {
        ReceitaModel receita = receitaMapper.map(receitaDTO);
        receita = receitaRepository.save(receita);
        return receitaMapper.map(receita);
    }

    // Mostrar Receita (ID)
    public ReceitaDTO mostrarReceita(Long id){
        Optional<ReceitaModel> receita = receitaRepository.findById(id);
        return receita.map(receitaMapper::map).orElse(null);
    }

    // Mostar Receitas
    public List<ReceitaDTO> mostrarTodasReceitas(){
        List<ReceitaModel> receitas = receitaRepository.findAll();
        return receitas.stream()
                .map(receitaMapper::map)
                .collect(Collectors.toList());
    }

    // Alterar Receita
    public ReceitaDTO alterarReceita(Long id, ReceitaDTO receitaDTO) {
        Optional<ReceitaModel> receita = receitaRepository.findById(id);
        if (receita.isPresent()) {
            ReceitaModel receitaAtualizada = receitaMapper.map(receitaDTO);
            receitaAtualizada.setId(id);
            ReceitaModel receitaSalva = receitaRepository.save(receitaAtualizada);
            return receitaMapper.map(receitaSalva);
        }
        return null;
    }

    // Deletar Receita
    public void deletarReceita(Long id){
        receitaRepository.deleteById(id);
    }

}
