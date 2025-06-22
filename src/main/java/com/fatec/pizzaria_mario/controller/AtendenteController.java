package com.fatec.pizzaria_mario.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/atendente")
public class AtendenteController {

    @GetMapping("/home")
    public String atendenteHomePage() {
        return "atendente-home";
    }

    @PostMapping("/definir-origem")
    public String definirOrigemPedidoAtendente(@RequestParam("origemPedido") String origemPedido, HttpSession session) {
        session.setAttribute("origemPedido", origemPedido);
        return "redirect:/atendente/cardapio/inicio";
    }

    @GetMapping("/novo-pedido")
    public String novoPedido(HttpSession session) {
        session.removeAttribute("origemPedido");
        session.removeAttribute("carrinho");
        return "redirect:/atendente/cardapio/inicio";
    }

    @GetMapping("/selecionar-origem")
    public String selecionarOrigem(HttpSession session, org.springframework.ui.Model model) {
        Object origem = session.getAttribute("origemPedido");
        if (origem != null) {
            model.addAttribute("origemSelecionada", origem.toString());
        }
        return "selecionar-origem-pedido";
    }
}