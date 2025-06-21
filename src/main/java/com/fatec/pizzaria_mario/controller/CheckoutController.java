package com.fatec.pizzaria_mario.controller;

import com.fatec.pizzaria_mario.domain.Carrinho;
import com.fatec.pizzaria_mario.domain.Usuario;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CheckoutController {

    @GetMapping("/checkout")
    public String exibirCheckout(HttpSession session, Model model, @AuthenticationPrincipal Usuario usuarioLogado) {
        Carrinho carrinho = (Carrinho) session.getAttribute("carrinho");
        if (carrinho == null || carrinho.getItens().isEmpty()) {
            return "redirect:/cardapio";
        }
        
        model.addAttribute("carrinho", carrinho);
        // Envia o usuário logado, com seus dados de endereço/telefone (se já existirem), para o formulário
        model.addAttribute("usuario", usuarioLogado); 

        return "checkout";
    }
}