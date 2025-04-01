package com.Acme.GestaoDeInventario.controller;

import com.Acme.GestaoDeInventario.dto.UsuarioDTO;
import com.Acme.GestaoDeInventario.mapper.UsuarioMapper;
import com.Acme.GestaoDeInventario.model.Usuario;
import com.Acme.GestaoDeInventario.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    @Autowired
    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping
    public ResponseEntity<UsuarioDTO> criarUsuario(@Valid @RequestBody UsuarioDTO usuarioDTO) {
        Usuario usuario = UsuarioMapper.INSTANCE.usuarioDTOToUsuario(usuarioDTO);

        Usuario usuarioCriado = usuarioService.salvarUsuario(usuario);

        UsuarioDTO usuarioDTOCriado = UsuarioMapper.INSTANCE.usuarioToUsuarioDTO(usuarioCriado);

        return ResponseEntity
                .created(URI.create("/usuarios/" + usuarioDTOCriado.getId()))
                .body(usuarioDTOCriado);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDTO> buscarUsuarioPorId(@PathVariable Long id) {
        Usuario usuario = usuarioService.buscarUsuarioPorId(id);
        UsuarioDTO usuarioDTO = UsuarioMapper.INSTANCE.usuarioToUsuarioDTO(usuario);

        if (usuario != null) {
            return ResponseEntity.ok(usuarioDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<UsuarioDTO>> listarUsuarios() {
        List<Usuario> usuarios = usuarioService.listarTodos();

        if (usuarios.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        List<UsuarioDTO> usuariosDTO = usuarios.stream()
                .map(UsuarioMapper.INSTANCE::usuarioToUsuarioDTO)
                .toList();

        return ResponseEntity.ok(usuariosDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioDTO> atualizarUsuario(@PathVariable long id, @RequestBody UsuarioDTO usuarioDTO) {
        Usuario usuario = UsuarioMapper.INSTANCE.usuarioDTOToUsuario(usuarioDTO);

        Usuario usuarioAtualizado = usuarioService.atualizarUsuario(id, usuario);

        UsuarioDTO usuarioDTOAtualizado = UsuarioMapper.INSTANCE.usuarioToUsuarioDTO(usuarioAtualizado);

        if (usuarioAtualizado != null) {
            return ResponseEntity.ok(usuarioDTOAtualizado);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarUsuario(@PathVariable Long id) {
        Usuario usuario = usuarioService.buscarUsuarioPorId(id);

        if (usuario != null) {
            usuarioService.deletarUsuario(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}