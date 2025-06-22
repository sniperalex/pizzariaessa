package com.fatec.pizzaria_mario.controller;

import com.fatec.pizzaria_mario.domain.Produto;
import com.fatec.pizzaria_mario.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
// MUDANÇA CRÍTICA: Prefixo /produtos para evitar conflito com a API REST.
@RequestMapping("/produtos")
public class ProdutoController {

    @Autowired
    private ProdutoRepository produtoRepository;

    @GetMapping
    public List<Produto> listarProdutos(){
        return produtoRepository.findAll();
    }

    @GetMapping("/{id}/imagem")
    public ResponseEntity<byte[]> getImagemProduto(@PathVariable String id) {
        Optional<Produto> produtoOpt = produtoRepository.findById(id);
        if (produtoOpt.isEmpty() || produtoOpt.get().getImagem() == null) {
            return ResponseEntity.notFound().build();
        }
        Produto produto = produtoOpt.get();
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(produto.getImagemTipo()))
                .body(produto.getImagem());
    }
}