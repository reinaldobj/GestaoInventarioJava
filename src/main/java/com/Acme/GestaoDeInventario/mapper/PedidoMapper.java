package com.Acme.GestaoDeInventario.mapper;

import com.Acme.GestaoDeInventario.dto.PedidoDTO;
import com.Acme.GestaoDeInventario.model.Pedido;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {PedidoProdutoMapper.class})
public interface PedidoMapper {
    PedidoMapper INSTANCE = Mappers.getMapper(PedidoMapper.class);

    PedidoDTO pedidoToPedidoDTO(Pedido pedido);
    Pedido pedidoDTOToPedido(PedidoDTO pedidoDTO);
}
