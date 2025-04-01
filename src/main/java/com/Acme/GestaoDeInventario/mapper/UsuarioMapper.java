package com.Acme.GestaoDeInventario.mapper;

import com.Acme.GestaoDeInventario.dto.UsuarioDTO;
import com.Acme.GestaoDeInventario.model.Usuario;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UsuarioMapper {
    UsuarioMapper INSTANCE = Mappers.getMapper(UsuarioMapper.class);

    UsuarioDTO usuarioToUsuarioDTO(Usuario usuario);
    Usuario usuarioDTOToUsuario(UsuarioDTO usuarioDTO);
}
