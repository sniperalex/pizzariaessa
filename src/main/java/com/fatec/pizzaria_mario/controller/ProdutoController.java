package com.fatec.pizzaria_mario.controller;

import com.fatec.pizzaria_mario.domain.Produto;
import com.fatec.pizzaria_mario.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/produtos")
public class ProdutoController {

    @Autowired
    private ProdutoRepository produtoRepository;

    @GetMapping
    public List<Produto> listarProdutos(){
        return produtoRepository.findAll();
    }
}