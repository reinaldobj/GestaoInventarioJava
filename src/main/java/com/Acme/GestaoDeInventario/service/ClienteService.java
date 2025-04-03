package com.Acme.GestaoDeInventario.service;

import com.Acme.GestaoDeInventario.model.Cliente;
import com.Acme.GestaoDeInventario.model.Usuario;
import com.Acme.GestaoDeInventario.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClienteService {
    private final ClienteRepository clienteRepository;
    private final UsuarioService usuarioService;

    @Autowired
    public ClienteService(ClienteRepository clienteRepository, UsuarioService usuarioService) {
        this.clienteRepository = clienteRepository;
        this.usuarioService = usuarioService;
    }

    //Salvar Cliente
    public Cliente salvarCliente(Cliente cliente) {
        if (cliente.getEndereco() == null || cliente.getEndereco().isEmpty()) {
            throw new RuntimeException("O cliente deve ter um endereço.");
        }

        if (cliente.getNumero() == 0) {
            throw new RuntimeException("O cliente deve ter um número.");
        }

        if (cliente.getTelefone() == null || cliente.getTelefone().isEmpty()) {
            throw new RuntimeException("O cliente deve ter um telefone.");
        }

        if (cliente.getUsuario() == null) {
            throw new RuntimeException("O cliente deve ter um usuário.");
        }

        // Verifica se o usuário existe
        Usuario usuario = usuarioService.buscarUsuarioPorId(cliente.getUsuario().getId());

        //Se o usuário existe vincula ao cliente, caso contrário cria um novo
        if (usuario.getId() != 0) {
            cliente.setUsuario(usuario);
        } else {
            usuario = usuarioService.salvarUsuario(usuario);
            cliente.setUsuario(usuario);
        }

        return clienteRepository.save(cliente);
    }

    //Alterar Cliente
    public Cliente alterarCliente(Cliente cliente) {
        Cliente clienteDb = clienteRepository.findById(cliente.getId())
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));

        if (cliente.getEndereco() != null)
            clienteDb.setEndereco(cliente.getEndereco());

        if (cliente.getNumero() != 0)
            clienteDb.setNumero(cliente.getNumero());

        if (cliente.getTelefone() != null)
            clienteDb.setTelefone(cliente.getTelefone());

        if (cliente.getUsuario() != null)
            clienteDb.setUsuario(cliente.getUsuario());

        return clienteRepository.save(cliente);
    }

    //Buscar Cliente por ID
    public Cliente buscarPorId(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
    }

    //Deletar Cliente
    public void deletar(Long id) {
        Cliente cliente = buscarPorId(id);
        clienteRepository.delete(cliente);
    }

    //Listar todos os Clientes
    public List<Cliente> listarTodos() {
        return clienteRepository.findAll();
    }
}
