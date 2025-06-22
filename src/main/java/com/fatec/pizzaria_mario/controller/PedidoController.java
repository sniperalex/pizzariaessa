package com.fatec.pizzaria_mario.controller;

import com.fatec.pizzaria_mario.domain.*;
import com.fatec.pizzaria_mario.repository.PedidoRepository;
import com.fatec.pizzaria_mario.repository.UsuarioRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.List;

@Controller
public class PedidoController {

    @Autowired
    private PedidoRepository pedidoRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;

    @PostMapping("/pedidos")
    public String finalizarPedido(@ModelAttribute Usuario usuarioForm, @RequestParam("formaPagamento") String formaPagamento, @AuthenticationPrincipal Usuario usuarioLogado, HttpSession session) {
        Carrinho carrinho = (Carrinho) session.getAttribute("carrinho");
        if (carrinho == null || carrinho.getItens().isEmpty()) { return "redirect:/cardapio"; }

        usuarioLogado.setNomeCompleto(usuarioForm.getNomeCompleto());
        usuarioLogado.setTelefone(usuarioForm.getTelefone());
        usuarioLogado.setEndereco(usuarioForm.getEndereco());
        usuarioRepository.save(usuarioLogado);

        Pedido pedido = new Pedido();
        pedido.setUsuarioId(usuarioLogado.getId());
        pedido.setNomeCliente(usuarioLogado.getNomeCompleto());
        pedido.setEnderecoEntrega(usuarioLogado.getEndereco());
        pedido.setItens(carrinho.getItens());
        pedido.setTotal(carrinho.getTotal());
        pedido.setDataHora(LocalDateTime.now());
        pedido.setStatus(StatusPedido.PENDENTE);
        pedido.setFormaPagamento(formaPagamento);
        pedidoRepository.save(pedido);
        session.removeAttribute("carrinho");
        
        return "redirect:/pedidos/" + pedido.getId() + "/confirmado";
    }

    @GetMapping("/pedidos/{id}/confirmado")
    public String pedidoConfirmado(@PathVariable String id, Model model) {
        pedidoRepository.findById(id).ifPresent(pedido -> model.addAttribute("pedido", pedido));
        return "pedido-confirmado";
    }

    @GetMapping("/meus-pedidos")
    public String meusPedidos(@AuthenticationPrincipal Usuario usuarioLogado, Model model, RedirectAttributes redirectAttributes) {
        if (usuarioLogado == null || usuarioLogado.getId() == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Usuário inválido. Faça login novamente.");
            return "redirect:/login";
        }
        try {
            List<Pedido> pedidos = pedidoRepository.findByUsuarioIdOrderByDataHoraDesc(usuarioLogado.getId());
            model.addAttribute("pedidos", pedidos != null ? pedidos : java.util.Collections.emptyList());
        } catch (Exception e) {
            model.addAttribute("pedidos", java.util.Collections.emptyList());
            model.addAttribute("errorMessage", "Erro ao buscar pedidos: " + e.getMessage());
        }
        return "meus-pedidos";
    }

    @PostMapping("/pedidos/alterar-status")
    public String alterarStatusPedido(@RequestParam("pedidoId") String pedidoId, @RequestParam("status") StatusPedido status, RedirectAttributes redirectAttributes) {
        pedidoRepository.findById(pedidoId).ifPresent(pedido -> {
            pedido.setStatus(status);
            pedidoRepository.save(pedido);
            redirectAttributes.addFlashAttribute("successMessage", "Status do pedido #" + pedidoId.substring(0, 8) + " atualizado!");
        });
        return "redirect:/admin/pedidos";
    }
}