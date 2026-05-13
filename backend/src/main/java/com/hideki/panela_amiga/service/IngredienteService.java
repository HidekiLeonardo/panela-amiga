package com.hideki.panela_amiga.service;

import com.hideki.panela_amiga.dto.IngredienteDTO;
import com.hideki.panela_amiga.exception.IngredienteNotFoundException;
import com.hideki.panela_amiga.mapper.IngredienteMapper;
import com.hideki.panela_amiga.model.IngredienteModel;
import com.hideki.panela_amiga.repository.IngredienteRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class IngredienteService {

    private final IngredienteRepository ingredienteRepository;
    private final IngredienteMapper ingredienteMapper;

    public IngredienteService(IngredienteRepository ingredienteRepository, IngredienteMapper ingredienteMapper) {
        this.ingredienteRepository = ingredienteRepository;
        this.ingredienteMapper = ingredienteMapper;
    }

    // Adicionar Ingrediente
    public IngredienteDTO addIngrediente(IngredienteDTO ingredienteDTO) {
        validarIngrediente(ingredienteDTO);
        IngredienteModel ingrediente = ingredienteMapper.toModel(ingredienteDTO);
        ingrediente = ingredienteRepository.save(ingrediente);
        return ingredienteMapper.toDTO(ingrediente);
    }

    // Mostrar Ingrediente(ID)
    public IngredienteDTO mostrarIngrediente(Long id) {
        Optional<IngredienteModel> ingrediente = ingredienteRepository.findById(id);
        return ingrediente
                .map(ingredienteMapper::toDTO)
                .orElseThrow(() -> new IngredienteNotFoundException("Ingrediente com o ID " + id + " não foi encontrado."));
    }

    // Mostrar Ingredientes
    public List<IngredienteDTO> mostrarTodosIngredientes() {
        List<IngredienteModel> ingredientes = ingredienteRepository.findAll();
        return ingredientes.stream()
                .map(ingredienteMapper::toDTO)
                .collect(Collectors.toList());
    }

    // Alterar Ingrediente
    public IngredienteDTO alterarIngrediente(Long id, IngredienteDTO ingredienteDTO) {
        validarIngrediente(ingredienteDTO);
        IngredienteModel ingrediente = ingredienteRepository.findById(id)
                .orElseThrow(() -> new IngredienteNotFoundException("Ingrediente com o ID " + id + " não foi encontrado."));
        ingrediente.setNome(ingredienteDTO.getNome());
        ingrediente.setMarca(ingredienteDTO.getMarca());
        ingrediente.setUnidadeDeMedida(ingredienteDTO.getUnidadeDeMedida());
        ingrediente.setFornecedor(ingredienteDTO.getFornecedor());
        ingrediente.setCustoUnitario(ingredienteDTO.getCustoUnitario());
        ingrediente.setQuantidadeEstoque(ingredienteDTO.getQuantidadeEstoque());
        ingrediente.setDataValidade(ingredienteDTO.getDataValidade());
        return ingredienteMapper.toDTO(ingredienteRepository.save(ingrediente));
    }

    // Deletar Ingrediente
    public void deletarIngrediente(Long id) {
        IngredienteModel ingrediente = ingredienteRepository.findById(id)
                .orElseThrow(() -> new IngredienteNotFoundException("Ingrediente com o ID " + id + " não foi encontrado."));
        if (ingrediente != null) {
            ingredienteRepository.deleteById(id);
        }
    }

    // Buscar por Nome
    public List<IngredienteDTO> buscarPorNome(String nome){
        List<IngredienteModel> ingredientes = ingredienteRepository.findByNomeContainingIgnoreCase(nome);
        return ingredientes.stream()
                .map(ingredienteMapper::toDTO)
                .collect(Collectors.toList());
    }

    // Buscar por Marca
    public List<IngredienteDTO> buscarPorMarca(String marca) {
        List<IngredienteModel> ingredientes = ingredienteRepository.findByMarca(marca);
        return ingredientes.stream()
                .map(ingredienteMapper::toDTO)
                .collect(Collectors.toList());
    }

    // Buscar por Fornecedor
    public List<IngredienteDTO> buscarPorFornecedor(String fornecedor) {
        List<IngredienteModel> ingredientes = ingredienteRepository.findByFornecedor(fornecedor);
        return ingredientes.stream()
                .map(ingredienteMapper::toDTO)
                .collect(Collectors.toList());
    }

    // Buscar Ingredientes Proximo do Vencimento
    public List<IngredienteDTO> buscarIngredienteProximoValidade(LocalDate data) {
        return ingredienteRepository.findAll().stream()
                .filter(ingrediente -> {
                    LocalDate validade = ingrediente.getDataValidade();
                    return !validade.isBefore(LocalDate.now()) &&
                            !validade.isAfter(data);
                })
                .map(ingredienteMapper::toDTO)
                .collect(Collectors.toList());
    }

    // Buscar Ingredientes Sem Estoque
    public List<IngredienteDTO> buscarIngredientesSemEstoque() {
        List<IngredienteModel> ingredientes = ingredienteRepository.findAll();
        return ingredientes.stream()
                .filter(ingrediente ->
                    ingrediente.getQuantidadeEstoque() == 0)
                .map(ingredienteMapper::toDTO)
                .collect(Collectors.toList());
    }

    // Atualizar Estoque
    public IngredienteDTO atualizarEstoqueIngrediente(Long id, Long novaQuantidade) {
        IngredienteModel ingrediente = ingredienteRepository.findById(id)
                .orElseThrow(() -> new IngredienteNotFoundException("Ingrediente com o ID " + id + " não foi encontrado."));
        ingrediente.setQuantidadeEstoque(novaQuantidade);
        return ingredienteMapper.toDTO(ingredienteRepository.save(ingrediente));
    }

    // Validação do Ingrediente
    private void validarIngrediente(IngredienteDTO dto) {
        if (dto.getNome() == null || dto.getNome().isBlank()) {
            throw new IllegalArgumentException("O nome do ingrediente é obrigatório.");
        }
        if (dto.getMarca() == null || dto.getMarca().isBlank()) {
            throw new IllegalArgumentException("O ingrediente deve ter uma marca.");
        }
        if (dto.getUnidadeDeMedida() == null) {
            throw new IllegalArgumentException("O preenchimento da unidade de medida é obrigatório.");
        }
        if (dto.getDataValidade() == null) {
            throw new IllegalArgumentException("Data de validade é obrigatória.");
        }
        if (dto.getQuantidadeEstoque() != null && dto.getQuantidadeEstoque() < 0) {
            throw new IllegalArgumentException("Quantidade em estoque não pode ser negativa.");
        }
        if (dto.getCustoUnitario() != null && dto.getCustoUnitario().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Custo unitário não pode ser negativo.");
        }
    }
}
