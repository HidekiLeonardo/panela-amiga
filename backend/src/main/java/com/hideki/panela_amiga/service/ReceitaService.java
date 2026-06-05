package com.hideki.panela_amiga.service;

import com.hideki.panela_amiga.dto.ReceitaDTO;
import com.hideki.panela_amiga.exception.ReceitaNotFoundException;
import com.hideki.panela_amiga.mapper.IngredienteReceitaMapper;
import com.hideki.panela_amiga.mapper.ReceitaMapper;
import com.hideki.panela_amiga.model.ReceitaModel;
import com.hideki.panela_amiga.model.UsuarioModel;
import com.hideki.panela_amiga.model.enums.CategoriaReceita;
import com.hideki.panela_amiga.repository.ReceitaRepository;
import com.hideki.panela_amiga.repository.UsuarioRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReceitaService {

    private final ReceitaRepository receitaRepository;
    private final ReceitaMapper receitaMapper;
    private final IngredienteReceitaMapper ingredienteReceitaMapper;
    private final UsuarioRepository usuarioRepository;

    public ReceitaService(ReceitaRepository receitaRepository, ReceitaMapper receitaMapper, IngredienteReceitaMapper ingredienteReceitaMapper, UsuarioRepository usuarioRepository) {
        this.receitaRepository = receitaRepository;
        this.receitaMapper = receitaMapper;
        this.ingredienteReceitaMapper = ingredienteReceitaMapper;
        this.usuarioRepository = usuarioRepository;
    }

    // Adicionar Receita
    public ReceitaDTO addReceita(ReceitaDTO receitaDTO) {
        validarReceita(receitaDTO);
        UsuarioModel usuario = getUsuarioLogado();
        ReceitaModel receita = receitaMapper.toModel(receitaDTO);
        receita.setUsuario(usuario);
        BigDecimal custoTotal = receita.getIngredientes().stream()
                .map(item -> {
                    BigDecimal quantidade = item.getQuantidade();
                    BigDecimal custoUnitario = item.getIngrediente().getCustoUnitario();
                    return custoUnitario.multiply(quantidade);
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        receita.setCustoTotal(custoTotal);
        receita = receitaRepository.save(receita);
        return receitaMapper.toDTO(receita);
    }

    // Mostrar Receita (ID)
    public ReceitaDTO mostrarReceita(Long id) {
        Optional<ReceitaModel> receita = receitaRepository.findById(id);
        return receita
                .map(receitaMapper::toDTO)
                .orElseThrow(() -> new ReceitaNotFoundException("Receita com o ID " + id + " não foi encontrado."));
    }

    // Mostar Receitas
    public List<ReceitaDTO> mostrarTodasReceitas() {
        UsuarioModel usuario = getUsuarioLogado();
        List<ReceitaModel> receitas = receitaRepository.findAllByUsuario(usuario);
        return receitas.stream()
                .map(receitaMapper::toDTO)
                .collect(Collectors.toList());
    }

    // Alterar Receita
    public ReceitaDTO alterarReceita(Long id, ReceitaDTO receitaDTO) {
        validarReceita(receitaDTO);
        ReceitaModel receita = receitaRepository.findById(id)
                .orElseThrow(() -> new ReceitaNotFoundException("Receita com o ID " + id + " não foi encontrado."));
        receita.setNome(receitaDTO.getNome());

        var ingredientes = receitaDTO.getIngredientes()
                        .stream()
                        .map(item -> ingredienteReceitaMapper.toModel(item, receita))
                        .collect(Collectors.toList());

        receita.getIngredientes().clear();
        receita.getIngredientes().addAll(ingredientes);

        receita.setCategoria(receitaDTO.getCategoria());
        receita.setModoPreparo(receitaDTO.getModoPreparo());
        receita.setTempoPreparo(receitaDTO.getTempoPreparo());
        receita.setPorcoes(receitaDTO.getPorcoes());
        receita.setRendimento(receitaDTO.getRendimento());
        receita.setCustoTotal(receitaDTO.getCustoTotal());
        receita.setPrecoVenda(receitaDTO.getPrecoVenda());
        return receitaMapper.toDTO(receitaRepository.save(receita));
    }

    // Deletar Receita
    public void deletarReceita(Long id) {
        ReceitaModel receita = receitaRepository.findById(id)
                        .orElseThrow(() -> new ReceitaNotFoundException("Receita com o ID " + id + " não foi encontrado."));
        receita.setAtivo(false);
        receitaRepository.save(receita);
    }

    // Buscar por Nome
    public List<ReceitaDTO> buscarPorNome(String nome) {
        List<ReceitaModel> receitas = receitaRepository.findByNomeContainingIgnoreCase(nome);
        return receitas.stream()
                .map(receitaMapper::toDTO)
                .collect(Collectors.toList());
    }

    // Buscar por Categoria
    public List<ReceitaDTO> buscarPorCategoria(CategoriaReceita categoria) {
        UsuarioModel usuario = getUsuarioLogado();
        List<ReceitaModel> receitas = receitaRepository.findByCategoriaAndUsuario(categoria, usuario);
        return receitas.stream()
                .map(receitaMapper::toDTO)
                .collect(Collectors.toList());
    }

    // Calcular CustoTotal Receita
    public BigDecimal custoTotalReceita(Long id) {
        ReceitaModel receita = receitaRepository.findById(id)
                .orElseThrow(() -> new ReceitaNotFoundException("Receita com o ID " + id + " não foi encontrado."));
        return receita.getCustoTotal() != null ? receita.getCustoTotal() : BigDecimal.ZERO;
    }

    // Validação da Receita
    private void validarReceita(ReceitaDTO dto) {
        if (dto.getNome() == null || dto.getNome().isBlank()) {
            throw new IllegalArgumentException("O nome da receita é obrigatório.");
        }
        if (dto.getPorcoes() <= 0){
            throw new IllegalArgumentException("A quantidade de porções da receita deve ser maior que 0.");
        }
        if (dto.getModoPreparo() == null || dto.getModoPreparo().isBlank()) {
            throw new IllegalArgumentException("O modo de preparo é obrigatório.");
        }
        if (dto.getIngredientes() == null || dto.getIngredientes().isEmpty()) {
            throw new IllegalArgumentException("A lista de ingredientes é obrigatória.");
        }
    }

    private UsuarioModel getUsuarioLogado() {
        String email = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não autenticado."));
    }
}