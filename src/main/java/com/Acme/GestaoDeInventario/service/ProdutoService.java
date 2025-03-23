package com.Acme.GestaoDeInventario.service;

import com.Acme.GestaoDeInventario.exception.ProdutoInvalidoException;
import com.Acme.GestaoDeInventario.exception.ProdutoNaoEncontradoException;
import com.Acme.GestaoDeInventario.model.Produto;
import com.Acme.GestaoDeInventario.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.List;

@Service
public class ProdutoService {
    private  final ProdutoRepository produtoRepository;

    @Autowired
    public ProdutoService(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    // Método para salvar um produto
    public Produto salvarProduto(@Valid Produto produto) {
        if(produto.getNome() == null || produto.getNome().isEmpty()){
            throw new ProdutoInvalidoException("O produto deve ter  um nome");
        }

        if(produto.getDescricao() == null || produto.getDescricao().isEmpty()){
            throw new ProdutoInvalidoException("O produto deve ter uma descrição");
        }

        if(produto.getQuantidade() <= 0){
            throw new ProdutoInvalidoException("A quantidade deve ser maior ou igual a zero");
        }

        if (produto.getPreco() <= 0) {
            throw new ProdutoInvalidoException("O preço deve ser maior ou igual a zero");
        }

        return produtoRepository.save(produto);
    }

    // Método para buscar um produto por ID
    public Produto buscarProdutoPorId(Long id) {
        return produtoRepository.findById(id)
                .orElseThrow(() -> new ProdutoNaoEncontradoException("Produto não encontrado"));

    }

    // Método para buscar um produto por nome
    public Produto buscarProdutoPorNome(String nome) {
        var produto = produtoRepository.findByNome(nome).getFirst();

        if(produto == null)
            throw new ProdutoNaoEncontradoException("Produto não encontrado");

        return produto;
    }

    // Método para listar todos os produtos
    public List<Produto> listarTodos() {
        return produtoRepository.findAll();
    }

    // Método para atualizar um produto
    public Produto atualizarProduto(Long id, Produto produto) {
        Produto produtoDb = produtoRepository.findById(id).orElse(null);
        if (produtoDb != null) {
            produtoDb.setNome(produto.getNome());
            produtoDb.setDescricao(produto.getDescricao());
            produtoDb.setPreco(produto.getPreco());
            produtoDb.setQuantidade(produto.getQuantidade());
            return produtoRepository.save(produtoDb);
        }
        return null;
    }

    // Método para deletar um produto
    public void deletarProduto(Long id) {
        produtoRepository.deleteById(id);
    }
}
