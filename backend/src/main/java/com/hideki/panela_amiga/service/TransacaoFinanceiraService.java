package com.hideki.panela_amiga.service;

import com.hideki.panela_amiga.dto.TransacaoFinanceiraDTO;
import com.hideki.panela_amiga.exception.EstoqueInsuficienteException;
import com.hideki.panela_amiga.exception.TransacaoFinanceiraNotFoundException;
import com.hideki.panela_amiga.mapper.TransacaoFinanceiraMapper;
import com.hideki.panela_amiga.model.*;
import com.hideki.panela_amiga.model.enums.OrigemTransacao;
import com.hideki.panela_amiga.model.enums.TipoTransacao;
import com.hideki.panela_amiga.repository.IngredienteRepository;
import com.hideki.panela_amiga.repository.ReceitaRepository;
import com.hideki.panela_amiga.repository.TransacaoFinanceiraRepository;
import com.hideki.panela_amiga.repository.UsuarioRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TransacaoFinanceiraService {

    private final TransacaoFinanceiraRepository transacaoFinanceiraRepository;
    private final TransacaoFinanceiraMapper transacaoFinanceiraMapper;
    private final ReceitaRepository receitaRepository;
    private final IngredienteRepository ingredienteRepository;
    private final UsuarioRepository usuarioRepository;

    public TransacaoFinanceiraService(TransacaoFinanceiraRepository transacaoFinanceiraRepository, TransacaoFinanceiraMapper transacaoFinanceiraMapper, IngredienteRepository ingredienteRepository, ReceitaRepository receitaRepository, UsuarioRepository usuarioRepository) {
        this.transacaoFinanceiraRepository = transacaoFinanceiraRepository;
        this.transacaoFinanceiraMapper = transacaoFinanceiraMapper;
        this.receitaRepository = receitaRepository;
        this.ingredienteRepository = ingredienteRepository;
        this.usuarioRepository = usuarioRepository;
    }

// Adicionar Transacao
    @Transactional
    public TransacaoFinanceiraDTO addTransacao(TransacaoFinanceiraDTO transacaoFinanceiraDTO) {
        validarTransacao(transacaoFinanceiraDTO);
        UsuarioModel usuario = getUsuarioLogado();
        TransacaoFinanceiraModel transacaoFinanceira = transacaoFinanceiraMapper.toModel(transacaoFinanceiraDTO);
        transacaoFinanceira.setUsuario(usuario);
        transacaoFinanceira = transacaoFinanceiraRepository.save(transacaoFinanceira);
        if (transacaoFinanceira.getTipoTransacao() == TipoTransacao.ENTRADA) {
            consumirEstoque(transacaoFinanceira.getReceita());
        }
        return transacaoFinanceiraMapper.toDTO(transacaoFinanceira);
    }

    // Buscar Transacao(ID)
    public TransacaoFinanceiraDTO mostrarTransacaoID(Long id) {
        UsuarioModel usuario = getUsuarioLogado();
        Optional<TransacaoFinanceiraModel> transacaoFinanceira = transacaoFinanceiraRepository.findByIdAndUsuario(id, usuario);
                return transacaoFinanceira
                        .map(transacaoFinanceiraMapper::toDTO)
                        .orElseThrow(() -> new TransacaoFinanceiraNotFoundException("Transação não foi encontrada."));
    }

    // Exibir Transacoes
    public List<TransacaoFinanceiraDTO> mostrarTodasTransacoes() {
        UsuarioModel usuario = getUsuarioLogado();
        List<TransacaoFinanceiraModel> transacaoFinanceira = transacaoFinanceiraRepository.findAllByUsuario(usuario);
        return transacaoFinanceira.stream()
                .map(transacaoFinanceiraMapper::toDTO)
                .collect(Collectors.toList());
    }

    // Exibir TransacoesEntreDatas(Data)
    public List<TransacaoFinanceiraDTO> mostrarTransacoesEntreDatas(LocalDate inicio, LocalDate fim) {
        UsuarioModel usuario = getUsuarioLogado();
        List<TransacaoFinanceiraModel> transacoesFinanceiras = transacaoFinanceiraRepository.findByDataBetweenAndUsuario(inicio, fim, usuario);
        return transacoesFinanceiras.stream()
                .map(transacaoFinanceiraMapper::toDTO)
                .collect(Collectors.toList());
    }

    // Exibir Transacoes(Origem)
    public List<TransacaoFinanceiraDTO> mostrarTransacoesPorOrigem(OrigemTransacao origem) {
        UsuarioModel usuario = getUsuarioLogado();
        List<TransacaoFinanceiraModel> transacoesFinanceiras = transacaoFinanceiraRepository.findByOrigemTransacaoAndUsuario(origem, usuario);
        return transacoesFinanceiras.stream()
                .map(transacaoFinanceiraMapper::toDTO)
                .collect(Collectors.toList());
    }

    // Exibir Transacoes(TipoTransacao)
    public List<TransacaoFinanceiraDTO> mostrarTransacoesPorTipo(TipoTransacao tipoTransacao){
        UsuarioModel usuario = getUsuarioLogado();
        List<TransacaoFinanceiraModel> transacoesFinanceiras = transacaoFinanceiraRepository.findByTipoTransacaoAndUsuario(tipoTransacao, usuario);
        return transacoesFinanceiras.stream()
                .map(transacaoFinanceiraMapper::toDTO)
                .collect(Collectors.toList());
    }

    // Exibir Transacoes(ReceitaID)
    public List<TransacaoFinanceiraDTO> mostrarTransacoesPorReceitaID(Long receitaID) {
        UsuarioModel usuario = getUsuarioLogado();
        List<TransacaoFinanceiraModel> transacoesFinanceiras = transacaoFinanceiraRepository.findByReceitaIdAndUsuario(receitaID, usuario);
        return transacoesFinanceiras.stream()
                .map(transacaoFinanceiraMapper::toDTO)
                .collect(Collectors.toList());
    }

    private void consumirEstoque(ReceitaModel receita) {
        UsuarioModel usuario = getUsuarioLogado();
        ReceitaModel receitaModel = receitaRepository.findByIdAndUsuario(receita.getId(), usuario)
                .orElseThrow(() -> new RuntimeException("Receita não encontrada."));
        for (IngredienteReceita ir : receitaModel.getIngredientes()) {
            IngredienteModel ingredienteModel = ir.getIngrediente();
            BigDecimal quantidadeUsada = ir.getQuantidade();
            if (ingredienteModel.getQuantidadeEstoque().compareTo(quantidadeUsada) >= 0){
                ingredienteModel.setQuantidadeEstoque(ingredienteModel.getQuantidadeEstoque().subtract(quantidadeUsada));
                ingredienteRepository.save(ingredienteModel);
            } else {
                throw new EstoqueInsuficienteException("Estoque insuficiente para o ingrediente " + ingredienteModel.getNome() +".");
            }
        }
    }

    private void validarTransacao(TransacaoFinanceiraDTO dto) {
        if (dto.getTipoTransacao() == TipoTransacao.ENTRADA) {
            if (dto.getReceitaId() == null) {
                throw new IllegalArgumentException("É obrigatório ter um id de uma receita.");
            }
        }
        if (dto.getOrigemTransacao() == null) {
            throw new IllegalArgumentException("A origem da transação é obrigatório.");
        }
        if (dto.getPagamento() == null) {
            throw new IllegalArgumentException("A forma de pagamento é obrigatório.");
        }
        if (dto.getValor() == null || dto.getValor().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("O valor deve ser maior que zero.");
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
