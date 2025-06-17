package com.hideki.panela_amiga.service;

import com.hideki.panela_amiga.model.ReceitaModel;
import com.hideki.panela_amiga.repository.ReceitaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReceitaService {

    @Autowired
    private ReceitaRepository receitaRepository;

    // Adicionar Receita
    public ReceitaModel addReceita(ReceitaModel receita) {
        return receitaRepository.save(receita);
    }

    // Mostrar Receita (ID)
    public ReceitaModel mostrarReceita(Long id){
        Optional<ReceitaModel> receita = receitaRepository.findById(id);
        return receita.orElse(null);
    }

    // Mostar Receitas
    public List<ReceitaModel> mostrarTodasReceitas(){
        return receitaRepository.findAll();
    }

    // Alterar Receita
    public ReceitaModel alterarReceita(Long id, ReceitaModel receita) {
        if (receitaRepository.existsById(id)) {
            receita.setId(id);
            receitaRepository.save(receita);
        }
        return null;
    }

    // Deletar Receita
    public void deletarReceita(Long id){
        receitaRepository.deleteById(id);
    }

}
