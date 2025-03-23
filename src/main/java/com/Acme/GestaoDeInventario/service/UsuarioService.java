package com.Acme.GestaoDeInventario.service;

import com.Acme.GestaoDeInventario.exception.UsuarioInvalidoException;
import com.Acme.GestaoDeInventario.exception.UsuarioNaoEncontradoException;
import com.Acme.GestaoDeInventario.model.Usuario;
import com.Acme.GestaoDeInventario.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.PublicKey;
import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    @Autowired
    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public Usuario salvarUsuario(Usuario usuario) {
        if(usuario.getNome() == null || usuario.getNome().isEmpty()){
            throw new UsuarioInvalidoException("O usuário deve ter um nome.");
        }

        if(usuario.getEmail() == null || usuario.getEmail().isEmpty()){
            throw new UsuarioInvalidoException("O usuário deve ter um email.");
        }

        if(usuario.getEndereco() == null || usuario.getEndereco().isEmpty()){
            throw new UsuarioInvalidoException("O usuário deve ter um endereço.");
        }

        if(usuario.getTelefone() == null || usuario.getTelefone().isEmpty()){
            throw new UsuarioInvalidoException("O usuário deve ter um telefone.");
        }

        if(usuario.getTipoUsuario() == null){
            throw new UsuarioInvalidoException("O usuário deve ter um tipo de usuário.");
        }

        return usuarioRepository.save(usuario);
    }

    public List<Usuario> listarTodos(){
        return usuarioRepository.findAll();
    }

    public Usuario buscarUsuarioPorId(Long id) {
        Optional<Usuario> usuario = usuarioRepository.findById(id);
        return usuario
                .orElseThrow(() -> new UsuarioNaoEncontradoException("Usuário não encontrado"));
    }

    public Usuario atualizarUsuario(long id, Usuario usuario){
        Usuario usuarioDb = usuarioRepository.findById(id)
                .orElseThrow(() -> new UsuarioNaoEncontradoException("Usuário não encontrado"));
        if(usuarioDb != null){
            usuarioDb.setNome(usuario.getNome());
            usuarioDb.setEmail(usuario.getEmail());
            usuarioDb.setEndereco(usuario.getEndereco());
            usuarioDb.setTelefone(usuario.getTelefone());

            usuarioRepository.save(usuarioDb);
        }

        return usuarioDb;
    }

    public void deletarUsuario(Long id){
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new UsuarioNaoEncontradoException("Usuário não encontrado"));

        usuarioRepository.deleteById(usuario.getId());
    }
}
