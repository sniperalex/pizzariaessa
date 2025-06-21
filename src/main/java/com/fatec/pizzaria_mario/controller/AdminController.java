package com.fatec.pizzaria_mario.controller;

import com.fatec.pizzaria_mario.domain.Pedido;
import com.fatec.pizzaria_mario.domain.Produto;
import com.fatec.pizzaria_mario.domain.StatusPedido;
import com.fatec.pizzaria_mario.domain.Usuario;
import com.fatec.pizzaria_mario.dto.ProdutoDTO;
import com.fatec.pizzaria_mario.repository.PedidoRepository;
import com.fatec.pizzaria_mario.repository.ProdutoRepository;
import com.fatec.pizzaria_mario.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired private ProdutoRepository produtoRepository;
    @Autowired private UsuarioRepository usuarioRepository;
    @Autowired private PedidoRepository pedidoRepository;
    @Autowired private PasswordEncoder passwordEncoder;

    @GetMapping("/dashboard")
    public String adminDashboard(Model model) {
        LocalDate hoje = LocalDate.now();
        LocalDateTime inicioDoDia = hoje.atStartOfDay();
        LocalDateTime fimDoDia = hoje.atTime(LocalTime.MAX);
        Map<StatusPedido, Long> contagemStatus = new EnumMap<>(StatusPedido.class);
        for(StatusPedido status : StatusPedido.values()) {
            long contagem = pedidoRepository.countByStatusAndDataHoraBetween(status, inicioDoDia, fimDoDia);
            contagemStatus.put(status, contagem);
        }
        model.addAttribute("contagemStatus", contagemStatus);
        return "admin-dashboard";
    }

    @GetMapping("/produtos")
    public String gerenciarProdutos(Model model) {
        List<Produto> produtos = produtoRepository.findAll(Sort.by(Sort.Direction.ASC, "categoria", "nome"));
        model.addAttribute("produtos", produtos);
        model.addAttribute("produtoDto", new ProdutoDTO());
        return "admin-produtos";
    }

    @PostMapping("/produtos")
    public String cadastrarProduto(@ModelAttribute ProdutoDTO produtoDto) throws IOException {
        Produto produto = new Produto();
        produto.setNome(produtoDto.getNome());
        produto.setDescricao(produtoDto.getDescricao());
        produto.setPreco(produtoDto.getPreco());
        produto.setCategoria(produtoDto.getCategoria());
        produto.setDisponibilidade(true);
        MultipartFile imagemFile = produtoDto.getImagemFile();
        if (imagemFile != null && !imagemFile.isEmpty()) {
            produto.setImagem(imagemFile.getBytes());
            produto.setImagemTipo(imagemFile.getContentType());
        }
        produtoRepository.save(produto);
        return "redirect:/admin/produtos";
    }

    @GetMapping("/usuarios")
    public String listarUsuarios(Model model) {
        model.addAttribute("usuarios", usuarioRepository.findAll());
        return "admin-usuarios";
    }

    @PostMapping("/usuarios")
    public String cadastrarUsuario(@RequestParam String login, @RequestParam String senha, @RequestParam String nomeCompleto, @RequestParam String email, @RequestParam String role, RedirectAttributes redirectAttributes) {
        if (usuarioRepository.findByLogin(login) != null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erro: Login '" + login + "' já existe!");
            return "redirect:/admin/usuarios";
        }
        Usuario novoUsuario = new Usuario();
        novoUsuario.setLogin(login);
        novoUsuario.setSenha(passwordEncoder.encode(senha));
        novoUsuario.setNomeCompleto(nomeCompleto);
        novoUsuario.setEmail(email);
        novoUsuario.addRole(role);
        usuarioRepository.save(novoUsuario);
        redirectAttributes.addFlashAttribute("successMessage", "Usuário '" + login + "' criado com sucesso!");
        return "redirect:/admin/usuarios";
    }

    @PostMapping("/usuarios/editar")
    public String editarUsuario(@RequestParam("id") String id, @RequestParam("login") String login, @RequestParam("nomeCompleto") String nomeCompleto, @RequestParam("email") String email, @RequestParam("role") String role, RedirectAttributes redirectAttributes) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(id);
        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            usuario.setLogin(login);
            usuario.setNomeCompleto(nomeCompleto);
            usuario.setEmail(email);
            usuario.getRoles().clear();
            usuario.addRole(role);
            usuarioRepository.save(usuario);
            redirectAttributes.addFlashAttribute("successMessage", "Usuário '" + login + "' atualizado!");
        }
        return "redirect:/admin/usuarios";
    }

    @PostMapping("/usuarios/remover/{id}")
    public String removerUsuario(@PathVariable String id, @AuthenticationPrincipal Usuario adminLogado, RedirectAttributes redirectAttributes) {
        if (adminLogado.getId().equals(id)) {
            redirectAttributes.addFlashAttribute("errorMessage", "Você não pode remover a si mesmo.");
            return "redirect:/admin/usuarios";
        }
        usuarioRepository.deleteById(id);
        redirectAttributes.addFlashAttribute("successMessage", "Usuário removido com sucesso.");
        return "redirect:/admin/usuarios";
    }
    
    @GetMapping("/pedidos")
    public String listarTodosPedidos(Model model, @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data, @RequestParam(required = false) String status, @RequestParam(required = false) String cliente) {
        List<Pedido> pedidos;
        if (data == null && !StringUtils.hasText(status) && !StringUtils.hasText(cliente)) {
            pedidos = pedidoRepository.findByDataHoraBetweenAndStatusIn(
                LocalDate.now().atStartOfDay(),
                LocalDate.now().atTime(LocalTime.MAX),
                Arrays.asList(StatusPedido.PENDENTE, StatusPedido.EM_PRODUCAO, StatusPedido.A_CAMINHO)
            );
        } else {
            if (StringUtils.hasText(cliente)) {
                pedidos = pedidoRepository.findByNomeClienteContaining(cliente);
            } else {
                pedidos = pedidoRepository.findAll(Sort.by(Sort.Direction.DESC, "dataHora"));
            }
            if (data != null) {
                pedidos = pedidos.stream().filter(p -> p.getDataHora().toLocalDate().isEqual(data)).collect(Collectors.toList());
            }
            if (StringUtils.hasText(status)) {
                StatusPedido statusEnum = StatusPedido.valueOf(status);
                pedidos = pedidos.stream().filter(p -> p.getStatus() == statusEnum).collect(Collectors.toList());
            }
        }
        model.addAttribute("pedidos", pedidos);
        model.addAttribute("dataFiltro", data);
        model.addAttribute("statusFiltro", status);
        model.addAttribute("clienteFiltro", cliente);
        return "admin-pedidos";
    }
}