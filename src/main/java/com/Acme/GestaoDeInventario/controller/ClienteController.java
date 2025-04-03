package com.Acme.GestaoDeInventario.controller;

import com.Acme.GestaoDeInventario.dto.ClienteDTO;
import com.Acme.GestaoDeInventario.mapper.ClienteMapper;
import com.Acme.GestaoDeInventario.model.Cliente;
import com.Acme.GestaoDeInventario.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@Controller
@RequestMapping("/clientes")
public class ClienteController {
    private  final ClienteService clienteService;

    @Autowired
    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    //Cadastrar Cliente
    @PostMapping
    public ResponseEntity<ClienteDTO> cadastrarCliente(@Valid @RequestBody ClienteDTO clienteDTO) {
        Cliente cliente = ClienteMapper.INSTANCE.clienteDTOToCliente(clienteDTO);

        clienteService.salvarCliente(cliente);

        clienteDTO = ClienteMapper.INSTANCE.clienteToClienteDTO(cliente);

        return ResponseEntity
                .created(URI.create("/clientes/" + clienteDTO.getId()))
                        .body(clienteDTO);
    }

    //Alterar Cliente
    @PutMapping("/{id}")
    public ResponseEntity<ClienteDTO> alterarCliente(@PathVariable long id, @RequestBody ClienteDTO clienteDTO) {
        Cliente cliente = ClienteMapper.INSTANCE.clienteDTOToCliente(clienteDTO);

        clienteService.alterarCliente(cliente);

        clienteDTO = ClienteMapper.INSTANCE.clienteToClienteDTO(cliente);

        return ResponseEntity
                .ok(clienteDTO);
    }

    //Buscar Cliente
    @GetMapping("/{id}")
    public ResponseEntity<ClienteDTO> buscarClientePorId(@PathVariable Long id) {
        Cliente cliente = clienteService.buscarPorId(id);
        ClienteDTO clienteDTO = ClienteMapper.INSTANCE.clienteToClienteDTO(cliente);

        if (cliente != null) {
            return ResponseEntity.ok(clienteDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    //Listar Clientes
    @GetMapping
    public ResponseEntity<List<ClienteDTO>> listarClientes() {
        List<Cliente> clientes = clienteService.listarTodos();

        if (clientes.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        List<ClienteDTO> clientesDTO = clientes.stream()
                .map(ClienteMapper.INSTANCE::clienteToClienteDTO)
                .toList();

        return ResponseEntity.ok(clientesDTO);
    }

    //Deletar Cliente
    @DeleteMapping
    public ResponseEntity<Void> deletarCliente(@PathVariable Long id) {
        clienteService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
