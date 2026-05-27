package com.hideki.panela_amiga.service;

import com.hideki.panela_amiga.dto.UsuarioDTO;
import com.hideki.panela_amiga.model.UsuarioModel;
import com.hideki.panela_amiga.repository.UsuarioRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService implements UserDetailsService {

    private final BCryptPasswordEncoder encoder;
    private final UsuarioRepository usuarioRepository;

    public UsuarioService(BCryptPasswordEncoder encoder, UsuarioRepository usuarioRepository) {
        this.encoder = encoder;
        this.usuarioRepository = usuarioRepository;
    }

    public void registrar(UsuarioDTO dto) {
        UsuarioModel usuarioModel = new UsuarioModel();
        usuarioModel.setEmail(dto.getEmail());
        usuarioModel.setSenha(encoder.encode(dto.getSenha()));
        usuarioRepository.save(usuarioModel);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Not Found"));
    }
}
