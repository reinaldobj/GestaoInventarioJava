package com.Acme.GestaoDeInventario.service;

import com.Acme.GestaoDeInventario.model.Produto;
import com.Acme.GestaoDeInventario.repository.ProdutoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ProdutoServiceTest {

    @InjectMocks
    private ProdutoService produtoService;

    @Mock
    private ProdutoRepository produtoRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deveSalvarProduto() {
        Produto produto = new Produto("Notebook", "Dell", 3000.0, 10);

        when(produtoRepository.save(produto)).thenReturn(produto);

        Produto resultado = produtoService.salvarProduto(produto);

        assertNotNull(resultado);
        assertEquals("Notebook", resultado.getNome());
        verify(produtoRepository, times(1)).save(produto);
    }

    @Test
    void deveLancarExcecaoQuandoProdutoNaoForEncontrado() {
        long idInexistente = 99L;
        when(produtoRepository.findById(idInexistente)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> produtoService.buscarProdutoPorId(idInexistente));

        assertEquals("Produto não encontrado", exception.getMessage());

        verify(produtoRepository, times(1)).findById(idInexistente);
    }
}
