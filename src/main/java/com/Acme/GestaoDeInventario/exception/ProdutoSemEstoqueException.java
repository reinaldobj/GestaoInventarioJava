package com.Acme.GestaoDeInventario.exception;

public class ProdutoSemEstoqueException extends RuntimeException {
    public ProdutoSemEstoqueException(String mensagem) {
        super(mensagem);
    }
}
