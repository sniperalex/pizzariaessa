package com.fatec.pizzaria_mario.controller;

import com.fatec.pizzaria_mario.domain.Carrinho;
import com.fatec.pizzaria_mario.domain.Cliente;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CheckoutController {

    @GetMapping("/checkout")
    public String exibirCheckout(HttpSession session, Model model) {
        // Pega o carrinho da sessão para mostrar um resumo na página
        Carrinho carrinho = (Carrinho) session.getAttribute("carrinho");

        // Se o carrinho estiver vazio, não faz sentido ir para o checkout.
        // Redireciona para o cardápio.
        if (carrinho == null || carrinho.getItens().isEmpty()) {
            return "redirect:/cardapio";
        }
        
        // Adiciona o carrinho e um objeto Cliente vazio ao modelo para o formulário
        model.addAttribute("carrinho", carrinho);
        model.addAttribute("cliente", new Cliente());

        return "checkout"; // Nome do novo arquivo HTML
    }
}