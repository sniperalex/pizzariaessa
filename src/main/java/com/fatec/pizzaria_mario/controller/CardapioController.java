package com.fatec.pizzaria_mario.controller;

import com.fatec.pizzaria_mario.domain.Produto;
import com.fatec.pizzaria_mario.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class CardapioController {

    @Autowired
    private ProdutoRepository produtoRepository;

    // Mostra apenas as PIZZAS
    @GetMapping("/cardapio")
    public String exibirCardapio(Model model) {
        List<Produto> pizzas = produtoRepository.findByCategoria("Pizza");
        model.addAttribute("produtos", pizzas); 
        return "cardapio";
    }

    // Mostra apenas os ACOMPANHAMENTOS
    @GetMapping("/acompanhamentos")
    public String exibirAcompanhamentos(Model model) {
        List<Produto> acompanhamentos = produtoRepository.findByCategoria("Acompanhamento");
        model.addAttribute("produtos", acompanhamentos); 
        return "acompanhamentos"; 
    }

    // NOVO MÉTODO: Mostra apenas as BEBIDAS
    @GetMapping("/bebidas")
    public String exibirBebidas(Model model) {
        List<Produto> bebidas = produtoRepository.findByCategoria("Bebida");
        model.addAttribute("produtos", bebidas); 
        return "bebidas"; // Nome do novo arquivo HTML
    }
}