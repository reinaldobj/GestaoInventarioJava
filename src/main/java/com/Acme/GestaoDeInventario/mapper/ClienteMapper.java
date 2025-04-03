package com.Acme.GestaoDeInventario.mapper;

import com.Acme.GestaoDeInventario.dto.ClienteDTO;
import com.Acme.GestaoDeInventario.model.Cliente;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ClienteMapper {
    ClienteMapper INSTANCE = Mappers.getMapper(ClienteMapper.class);

    ClienteDTO clienteToClienteDTO(Cliente cliente);
    Cliente clienteDTOToCliente(ClienteDTO clienteDTO);
}
