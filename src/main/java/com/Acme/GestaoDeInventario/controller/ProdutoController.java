package com.Acme.GestaoDeInventario.controller;

import com.Acme.GestaoDeInventario.dto.ProdutoDTO;
import com.Acme.GestaoDeInventario.mapper.ProdutoMapper;
import com.Acme.GestaoDeInventario.model.Produto;
import com.Acme.GestaoDeInventario.service.ProdutoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/produtos")
@Validated
public class ProdutoController {
    private final ProdutoService produtoService;

    @Autowired
    public ProdutoController(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    @PostMapping
    public ResponseEntity<ProdutoDTO> criarProduto(@Valid @RequestBody ProdutoDTO produtoDTO) {
        Produto produto = ProdutoMapper.INSTANCE.produtoDTOToProduto(produtoDTO);

        Produto produtoCriado = produtoService.salvarProduto(produto);

        ProdutoDTO produtoDTOCriado = ProdutoMapper.INSTANCE.produtoToProdutoDTO(produtoCriado);

        return ResponseEntity
                .created(URI.create("/produtos" + produtoDTOCriado.getId()))
                .body(produtoDTOCriado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProdutoDTO> atualizarProduto(@PathVariable long id, @Valid @RequestBody ProdutoDTO produtoDTO) {
        Produto produto = ProdutoMapper.INSTANCE.produtoDTOToProduto(produtoDTO);

        Produto produtoAtualizado = produtoService.atualizarProduto(id, produto);

        ProdutoDTO produtoDTOAtualizado = ProdutoMapper.INSTANCE.produtoToProdutoDTO(produtoAtualizado);

        if (produtoAtualizado != null) {
            return ResponseEntity.ok(produtoDTOAtualizado);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarProduto(@PathVariable long id) {
        Produto produto = produtoService.buscarProdutoPorId(id);
        if (produto != null) {
            produtoService.deletarProduto(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<ProdutoDTO>> listarProdutos() {
        List<Produto> produtos = produtoService.listarTodos();

        List<ProdutoDTO> produtosDTO = produtos.stream()
                .map(ProdutoMapper.INSTANCE::produtoToProdutoDTO)
                .toList();

        return ResponseEntity.ok(produtosDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProdutoDTO> buscarProdutoPorId(@PathVariable long id) {
        Produto produto = produtoService.buscarProdutoPorId(id);

        ProdutoDTO produtoDTO = ProdutoMapper.INSTANCE.produtoToProdutoDTO(produto);

        return ResponseEntity.ok(produtoDTO);

    }
}
