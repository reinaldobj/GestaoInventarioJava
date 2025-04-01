package com.Acme.GestaoDeInventario.mapper;

import com.Acme.GestaoDeInventario.dto.ProdutoDTO;
import com.Acme.GestaoDeInventario.model.Produto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ProdutoMapper {
    ProdutoMapper INSTANCE = Mappers.getMapper(ProdutoMapper.class);

    ProdutoDTO produtoToProdutoDTO(Produto produto);
    Produto produtoDTOToProduto(ProdutoDTO produtoDTO);
}
