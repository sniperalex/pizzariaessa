package com.fatec.pizzaria_mario.controller;

import com.fatec.pizzaria_mario.domain.Carrinho;
import com.fatec.pizzaria_mario.domain.ItemPedido;
import com.fatec.pizzaria_mario.domain.Produto;
import com.fatec.pizzaria_mario.repository.ProdutoRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

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

    // NOVO: Tela inicial para escolha de tipo de pizza
    @GetMapping("/cardapio/inicio")
    public String escolherTipoPizza() {
        return "selecionar-sabor";
    }

    // NOVO: Tela para seleção de dois sabores (meio a meio)
    @GetMapping("/cardapio/meio-a-meio")
    public String selecionarMeioMeio(Model model) {
        List<Produto> pizzas = produtoRepository.findByCategoria("Pizza");
        model.addAttribute("pizzas", pizzas);
        model.addAttribute("meioMeioForm", new com.fatec.pizzaria_mario.dto.MeioMeioForm());
        return "selecionar-sabores-meio-a-meio";
    }

    // NOVO: Processa seleção de dois sabores e avança para acompanhamentos
    @PostMapping("/cardapio/meio-a-meio/selecionar")
    public String processarMeioMeio(com.fatec.pizzaria_mario.dto.MeioMeioForm meioMeioForm, Model model, HttpServletRequest request) {
        Optional<Produto> sabor1Opt = produtoRepository.findById(meioMeioForm.getSabor1());
        Optional<Produto> sabor2Opt = produtoRepository.findById(meioMeioForm.getSabor2());
        if (sabor1Opt.isPresent() && sabor2Opt.isPresent()) {
            Produto sabor1 = sabor1Opt.get();
            Produto sabor2 = sabor2Opt.get();
            Produto meioMeio = new Produto();
            meioMeio.setId(sabor1.getId() + "-" + sabor2.getId());
            meioMeio.setNome("Pizza Meio a Meio: " + sabor1.getNome() + " / " + sabor2.getNome());
            meioMeio.setDescricao("Metade: " + sabor1.getNome() + " - " + sabor1.getDescricao() + " | Metade: " + sabor2.getNome() + " - " + sabor2.getDescricao());
            meioMeio.setPreco(sabor1.getPreco().max(sabor2.getPreco()));
            meioMeio.setCategoria("Pizza Meio a Meio");
            meioMeio.setDisponibilidade(true);
            Carrinho carrinho = (Carrinho) request.getSession().getAttribute("carrinho");
            if (carrinho == null) {
                carrinho = new Carrinho();
                request.getSession().setAttribute("carrinho", carrinho);
            }
            carrinho.adicionarItem(meioMeio);
        }
        // Permanece na tela para permitir adicionar mais pizzas
        return "redirect:/cardapio/meio-a-meio";
    }

    // NOVO: Seleção visual de pizza de 1 sabor
    @GetMapping("/cardapio/um-sabor")
    public String selecionarPizzaUmSabor(Model model) {
        List<Produto> pizzas = produtoRepository.findByCategoria("Pizza");
        model.addAttribute("pizzas", pizzas);
        return "selecionar-pizza-um-sabor";
    }

    // NOVO: Processa seleção de pizza de 1 sabor
    @PostMapping("/cardapio/um-sabor/selecionar")
    public String processarPizzaUmSabor(String pizzaId) {
        // Aqui você pode adicionar a pizza ao carrinho ou salvar na sessão
        // Por enquanto, apenas redireciona para acompanhamentos
        return "redirect:/acompanhamentos";
    }
}