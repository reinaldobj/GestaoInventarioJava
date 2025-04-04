package com.Acme.GestaoDeInventario.controller;

import com.Acme.GestaoDeInventario.dto.JwtResponse;
import com.Acme.GestaoDeInventario.dto.LoginRequest;
import com.Acme.GestaoDeInventario.security.jwt.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager, JwtUtils jwtUtils) {
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getSenha())
            );

            String token = jwtUtils.gerarToken(loginRequest.getEmail());
            return ResponseEntity.ok(new JwtResponse(token));

        } catch (AuthenticationException ex) {
            return ResponseEntity.status(401).body("Email ou senha inválidos.");
        }
    }
}
