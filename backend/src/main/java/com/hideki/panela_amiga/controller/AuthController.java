package com.hideki.panela_amiga.controller;

import com.hideki.panela_amiga.dto.UsuarioDTO;
import com.hideki.panela_amiga.service.JwtService;
import com.hideki.panela_amiga.service.UsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UsuarioService usuarioService;
    private final JwtService jwtService;
    private final BCryptPasswordEncoder encoder;

    public AuthController(UsuarioService usuarioService, JwtService jwtService, BCryptPasswordEncoder encoder) {
        this.usuarioService = usuarioService;
        this.jwtService = jwtService;
        this.encoder = encoder;
    }

    @PostMapping("/register")
    public ResponseEntity<String> registrar(@RequestBody UsuarioDTO dto) {
        usuarioService.registrar(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Usuário cadastrado com sucesso!");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UsuarioDTO dto) {
        try {
            UserDetails usuario = usuarioService.loadUserByUsername(dto.getEmail());
            if (encoder.matches(dto.getSenha(), usuario.getPassword())){
                return ResponseEntity.ok(jwtService.gerarToken(dto.getEmail()));
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Credenciais inválidas...");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Credenciais inválidas...");
        }

    }

}
