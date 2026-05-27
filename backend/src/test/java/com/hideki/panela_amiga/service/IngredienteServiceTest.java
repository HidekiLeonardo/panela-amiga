package com.hideki.panela_amiga.service;

import com.hideki.panela_amiga.exception.IngredienteUsingException;
import com.hideki.panela_amiga.mapper.IngredienteMapper;
import com.hideki.panela_amiga.model.IngredienteModel;
import com.hideki.panela_amiga.repository.IngredienteRepository;
import com.hideki.panela_amiga.repository.ReceitaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class IngredienteServiceTest {

    @InjectMocks
    IngredienteService ingredienteService;
    @Mock
    IngredienteRepository ingredienteRepository;
    @Mock
    ReceitaRepository receitaRepository;
    @Mock
    IngredienteMapper ingredienteMapper;

    @Test
    void deletarIngrediente() {
        IngredienteModel ingrediente = new IngredienteModel();

        when(ingredienteRepository.findById(1L)).thenReturn(Optional.of(ingrediente));
        when(receitaRepository.existeReceitaAtivaComIngrediente(1L)).thenReturn(false);
        ingredienteService.deletarIngrediente(1L);

        verify(ingredienteRepository, times(1)).deleteById(1L);
    }

    @Test
    void deletarIngredienteEmUso() {
        IngredienteModel ingrediente = new IngredienteModel();

        when(ingredienteRepository.findById(1L)).thenReturn(Optional.of(ingrediente));
        when(receitaRepository.existeReceitaAtivaComIngrediente(1L)).thenReturn(true);

        assertThrows(IngredienteUsingException.class, () -> ingredienteService.deletarIngrediente(1L));
    }


}
