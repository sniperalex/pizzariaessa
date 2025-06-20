package com.fatec.pizzaria_mario.controller;

import com.fatec.pizzaria_mario.domain.Carrinho;
import com.fatec.pizzaria_mario.domain.Produto;
import com.fatec.pizzaria_mario.repository.ProdutoRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
@RequestMapping("/carrinho")
public class CarrinhoController {

    @Autowired
    private ProdutoRepository produtoRepository;

    @PostMapping("/adicionar")
    public String adicionarAoCarrinho(@RequestParam("produtoId") String produtoId, HttpServletRequest request) {
        HttpSession session = request.getSession();
        Carrinho carrinho = (Carrinho) session.getAttribute("carrinho");
        if (carrinho == null) {
            carrinho = new Carrinho();
            session.setAttribute("carrinho", carrinho);
        }
        Optional<Produto> produtoOpt = produtoRepository.findById(produtoId);
        produtoOpt.ifPresent(carrinho::adicionarItem);
        String referer = request.getHeader("Referer");
        return "redirect:" + (referer != null ? referer : "/cardapio");
    }

    @GetMapping("/resumo")
    public String exibirResumoCarrinho(HttpSession session, Model model) {
        Carrinho carrinho = (Carrinho) session.getAttribute("carrinho");
        if (carrinho == null) {
            carrinho = new Carrinho();
        }
        model.addAttribute("carrinho", carrinho);
        return "resumo-carrinho";
    }

    // NOVO MÉTODO: Atualiza a quantidade (aumentar ou diminuir)
    @PostMapping("/atualizar")
    public String atualizarCarrinho(@RequestParam("produtoId") String produtoId, @RequestParam("quantidade") int quantidade, HttpSession session) {
        Carrinho carrinho = (Carrinho) session.getAttribute("carrinho");
        if (carrinho != null) {
            carrinho.atualizarQuantidade(produtoId, quantidade);
        }
        return "redirect:/carrinho/resumo";
    }

    // NOVO MÉTODO: Remove um item do carrinho
    @PostMapping("/remover")
    public String removerDoCarrinho(@RequestParam("produtoId") String produtoId, HttpSession session) {
        Carrinho carrinho = (Carrinho) session.getAttribute("carrinho");
        if (carrinho != null) {
            carrinho.removerItem(produtoId);
        }
        return "redirect:/carrinho/resumo";
    }
}