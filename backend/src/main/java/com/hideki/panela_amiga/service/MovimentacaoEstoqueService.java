package com.hideki.panela_amiga.service;

import com.hideki.panela_amiga.dto.EntradaEstoqueDTO;
import com.hideki.panela_amiga.dto.TransacaoFinanceiraDTO;
import com.hideki.panela_amiga.exception.IngredienteNotFoundException;
import com.hideki.panela_amiga.model.IngredienteModel;
import com.hideki.panela_amiga.model.MovimentacaoEstoque;
import com.hideki.panela_amiga.model.UsuarioModel;
import com.hideki.panela_amiga.model.enums.OrigemTransacao;
import com.hideki.panela_amiga.model.enums.TipoMovimentacao;
import com.hideki.panela_amiga.model.enums.TipoTransacao;
import com.hideki.panela_amiga.repository.IngredienteRepository;
import com.hideki.panela_amiga.repository.MovimentacaoEstoqueRepository;
import com.hideki.panela_amiga.repository.UsuarioRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class MovimentacaoEstoqueService {
    private final MovimentacaoEstoqueRepository movimentacaoEstoqueRepository;
    private final IngredienteRepository ingredienteRepository;
    private final TransacaoFinanceiraService transacaoFinanceiraService;
    private final UsuarioRepository usuarioRepository;

    public MovimentacaoEstoqueService(MovimentacaoEstoqueRepository movimentacaoEstoqueRepository, IngredienteRepository ingredienteRepository, TransacaoFinanceiraService transacaoFinanceiraService, UsuarioRepository usuarioRepository) {
        this.movimentacaoEstoqueRepository = movimentacaoEstoqueRepository;
        this.ingredienteRepository = ingredienteRepository;
        this.transacaoFinanceiraService = transacaoFinanceiraService;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional
    public void registrarCompra(EntradaEstoqueDTO dto) {
        UsuarioModel usuario = getUsuarioLogado();
        IngredienteModel ingrediente = ingredienteRepository.findByIdAndUsuario(dto.getIngredienteId(), usuario)
                .orElseThrow(() -> new IngredienteNotFoundException("Ingrediente não encontrado."));
        MovimentacaoEstoque movimentacaoEstoque = new MovimentacaoEstoque();
        movimentacaoEstoque.setIngrediente(ingrediente);
        movimentacaoEstoque.setTipo(TipoMovimentacao.COMPRA);
        movimentacaoEstoque.setQuantidade(dto.getQuantidade());
        movimentacaoEstoque.setData(LocalDateTime.now());
        movimentacaoEstoque.setUsuario(usuario);
        movimentacaoEstoqueRepository.save(movimentacaoEstoque);

        ingrediente.setQuantidadeEstoque(ingrediente.getQuantidadeEstoque().add(dto.getQuantidade()));
        ingredienteRepository.save(ingrediente);

        BigDecimal valor = dto.getCustoUnitario().multiply(dto.getQuantidade());
        TransacaoFinanceiraDTO transacaoFinanceiraDTO = new TransacaoFinanceiraDTO();
        transacaoFinanceiraDTO.setTipoTransacao(TipoTransacao.SAIDA);
        transacaoFinanceiraDTO.setValor(valor);
        transacaoFinanceiraDTO.setPagamento(dto.getPagamento());
        transacaoFinanceiraDTO.setOrigemTransacao(OrigemTransacao.COMPRA);
        transacaoFinanceiraDTO.setData(LocalDate.now());
        transacaoFinanceiraService.addTransacao(transacaoFinanceiraDTO);
    }

    private UsuarioModel getUsuarioLogado() {
        String email = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não autenticado."));
    }
}
