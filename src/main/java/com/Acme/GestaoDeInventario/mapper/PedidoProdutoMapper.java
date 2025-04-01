package com.Acme.GestaoDeInventario.mapper;

import com.Acme.GestaoDeInventario.dto.PedidoProdutoDTO;
import com.Acme.GestaoDeInventario.model.PedidoProduto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PedidoProdutoMapper {
    PedidoProdutoMapper INSTANCE = Mappers.getMapper(PedidoProdutoMapper.class);

    @Mapping(target = "pedido", ignore = true)
    PedidoProdutoDTO pedidoProdutoToPedidoProdutoDTO(PedidoProduto pedidoProduto);

    PedidoProduto pedidoProdutoDTOToPedidoProduto(PedidoProdutoDTO pedidoProdutoDTO);
}
