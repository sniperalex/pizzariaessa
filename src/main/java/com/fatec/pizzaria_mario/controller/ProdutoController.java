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
@RequestMapping("/produtos")
public class ProdutoController {

    @Autowired
    private ProdutoRepository produtoRepository;

    @GetMapping
    public List<Produto> listarProdutos(){
        return produtoRepository.findAll();
    }

    // MÉTODO NOVO PARA SERVIR A IMAGEM
    @GetMapping("/{id}/imagem")
    public ResponseEntity<byte[]> getImagemProduto(@PathVariable String id) {
        // Busca o produto pelo ID
        Optional<Produto> produtoOpt = produtoRepository.findById(id);

        if (produtoOpt.isEmpty() || produtoOpt.get().getImagem() == null) {
            // Retorna 404 Not Found se o produto ou a imagem não existirem
            return ResponseEntity.notFound().build();
        }

        Produto produto = produtoOpt.get();
        // Constrói a resposta:
        // - Corpo: os bytes da imagem
        // - Cabeçalho Content-Type: o tipo da imagem (jpeg, png, etc)
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(produto.getImagemTipo()))
                .body(produto.getImagem());
    }
}