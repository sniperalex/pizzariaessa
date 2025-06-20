package com.fatec.pizzaria_mario.controller;

import com.fatec.pizzaria_mario.domain.*;
import com.fatec.pizzaria_mario.repository.ClienteRepository;
import com.fatec.pizzaria_mario.repository.PedidoRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Controller
public class PedidoController {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private PedidoRepository pedidoRepository;

    // Método que recebe os dados do formulário de checkout
    @PostMapping("/pedidos")
    public String finalizarPedido(
            @ModelAttribute Cliente cliente, 
            @RequestParam("formaPagamento") String formaPagamento,
            HttpSession session) {

        Carrinho carrinho = (Carrinho) session.getAttribute("carrinho");
        if (carrinho == null || carrinho.getItens().isEmpty()) {
            return "redirect:/cardapio"; // Carrinho vazio, não há o que finalizar
        }

        // 1. Salva o cliente
        clienteRepository.save(cliente);

        // 2. Cria o objeto Pedido
        Pedido pedido = new Pedido();
        pedido.setCliente(cliente);
        pedido.setItens(carrinho.getItens());
        pedido.setTotal(carrinho.getTotal());
        pedido.setDataHora(LocalDateTime.now());
        pedido.setStatus(StatusPedido.PENDENTE);
        pedido.setFormaPagamento(formaPagamento);
        
        // 3. Salva o pedido no banco de dados
        pedidoRepository.save(pedido);

        // 4. Limpa o carrinho da sessão
        session.removeAttribute("carrinho");
        
        // 5. Redireciona para a página de sucesso
        return "redirect:/pedidos/confirmado";
    }

    // Método que mostra a página de confirmação
    @GetMapping("/pedidos/confirmado")
    public String pedidoConfirmado() {
        return "pedido-confirmado";
    }
}