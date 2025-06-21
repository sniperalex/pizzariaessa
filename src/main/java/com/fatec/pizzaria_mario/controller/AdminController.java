package com.fatec.pizzaria_mario.controller;

import com.fatec.pizzaria_mario.domain.Pedido;
import com.fatec.pizzaria_mario.domain.Produto;
import com.fatec.pizzaria_mario.domain.Usuario;
import com.fatec.pizzaria_mario.dto.ProdutoDTO;
import com.fatec.pizzaria_mario.repository.PedidoRepository;
import com.fatec.pizzaria_mario.repository.ProdutoRepository;
import com.fatec.pizzaria_mario.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private ProdutoRepository produtoRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private PedidoRepository pedidoRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    // Métodos de Gerenciamento de Produto (sem alterações)
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
    
    // Métodos de Gerenciamento de Usuários
    @GetMapping("/usuarios")
    public String listarUsuarios(Model model) {
        List<Usuario> usuarios = usuarioRepository.findAll();
        model.addAttribute("usuarios", usuarios);
        return "admin-usuarios";
    }

    @PostMapping("/usuarios")
    public String cadastrarUsuario(
            @RequestParam String login,
            @RequestParam String senha,
            @RequestParam String nomeCompleto,
            @RequestParam String role,
            RedirectAttributes redirectAttributes) {
        if (usuarioRepository.findByLogin(login) != null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erro: Login '" + login + "' já existe!");
            return "redirect:/admin/usuarios";
        }
        Usuario novoUsuario = new Usuario();
        novoUsuario.setLogin(login);
        novoUsuario.setSenha(passwordEncoder.encode(senha));
        novoUsuario.setNomeCompleto(nomeCompleto);
        novoUsuario.addRole(role);
        usuarioRepository.save(novoUsuario);
        redirectAttributes.addFlashAttribute("successMessage", "Usuário '" + login + "' criado com sucesso!");
        return "redirect:/admin/usuarios";
    }

    @PostMapping("/usuarios/editar")
    public String editarUsuario(
            @RequestParam("id") String id,
            @RequestParam("login") String login,
            @RequestParam("nomeCompleto") String nomeCompleto,
            @RequestParam("role") String role,
            RedirectAttributes redirectAttributes) {
        
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(id);
        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            usuario.setLogin(login);
            usuario.setNomeCompleto(nomeCompleto);
            usuario.getRoles().clear();
            usuario.addRole(role);
            usuarioRepository.save(usuario);
            redirectAttributes.addFlashAttribute("successMessage", "Usuário '" + login + "' atualizado com sucesso!");
        }
        return "redirect:/admin/usuarios";
    }

    @PostMapping("/usuarios/remover/{id}")
    public String removerUsuario(@PathVariable String id, @AuthenticationPrincipal Usuario adminLogado, RedirectAttributes redirectAttributes) {
        if (adminLogado.getId().equals(id)) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erro: Você não pode remover a si mesmo.");
            return "redirect:/admin/usuarios";
        }
        usuarioRepository.deleteById(id);
        redirectAttributes.addFlashAttribute("successMessage", "Usuário removido com sucesso.");
        return "redirect:/admin/usuarios";
    }
    
    // Métodos de Gerenciamento de Pedidos
    @GetMapping("/pedidos")
    public String listarTodosPedidos(Model model) {
        List<Pedido> pedidos = pedidoRepository.findAll(Sort.by(Sort.Direction.DESC, "dataHora"));
        model.addAttribute("pedidos", pedidos);
        return "admin-pedidos";
    }
}