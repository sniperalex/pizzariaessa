package com.fatec.pizzaria_mario.controller;

import com.fatec.pizzaria_mario.domain.Pedido;
import com.fatec.pizzaria_mario.domain.Produto;
import com.fatec.pizzaria_mario.domain.StatusPedido;
import com.fatec.pizzaria_mario.domain.Usuario;
import com.fatec.pizzaria_mario.dto.ProdutoDTO;
import com.fatec.pizzaria_mario.repository.PedidoRepository;
import com.fatec.pizzaria_mario.repository.ProdutoRepository;
import com.fatec.pizzaria_mario.repository.UsuarioRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
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

    // ==========================================================
    // MÉTODO DO DASHBOARD
    // ==========================================================
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

    // ==========================================================
    // --- GERENCIAMENTO DE PRODUTOS (COM IDs CORRIGIDOS PARA STRING) ---
    // ==========================================================
    @GetMapping("/produtos")
    public String gerenciarProdutos(Model model) {
        if (!model.containsAttribute("produtoDto")) {
            model.addAttribute("produtoDto", new ProdutoDTO());
        }
        List<Produto> produtos = produtoRepository.findAll(Sort.by(Sort.Direction.ASC, "categoria", "nome"));
        model.addAttribute("produtos", produtos);
        return "admin-produtos";
    }

    @PostMapping("/produtos")
    public String cadastrarProduto(
            @Valid @ModelAttribute("produtoDto") ProdutoDTO produtoDto,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) throws IOException {
        
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.produtoDto", bindingResult);
            redirectAttributes.addFlashAttribute("produtoDto", produtoDto);
            return "redirect:/admin/produtos";
        }
        
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
        redirectAttributes.addFlashAttribute("successMessage", "Produto '" + produto.getNome() + "' cadastrado com sucesso!");
        return "redirect:/admin/produtos";
    }

    // <<< CORREÇÃO AQUI: ID alterado de Long para String >>>
    @GetMapping("/produtos/editar/{id}")
    public String exibirFormularioEdicaoProduto(@PathVariable String id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Produto> produtoOptional = produtoRepository.findById(id);
        if (produtoOptional.isPresent()) {
            Produto produto = produtoOptional.get();
            ProdutoDTO produtoDto = new ProdutoDTO();
            produtoDto.setId(produto.getId());
            produtoDto.setNome(produto.getNome());
            produtoDto.setDescricao(produto.getDescricao());
            produtoDto.setPreco(produto.getPreco());
            produtoDto.setCategoria(produto.getCategoria());
            
            model.addAttribute("produtoDto", produtoDto);
            
            return "admin-produto-editar"; 
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Produto não encontrado!");
            return "redirect:/admin/produtos";
        }
    }

    // <<< CORREÇÃO AQUI: ID alterado de Long para String >>>
    @PostMapping("/produtos/editar/{id}")
    public String processarEdicaoProduto(
            @PathVariable String id, 
            @Valid @ModelAttribute("produtoDto") ProdutoDTO produtoDto, 
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) throws IOException {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.produtoDto", bindingResult);
            redirectAttributes.addFlashAttribute("produtoDto", produtoDto);
            return "redirect:/admin/produtos/editar/" + id;
        }

        Optional<Produto> produtoExistenteOpt = produtoRepository.findById(id);
        if (produtoExistenteOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erro: Produto não encontrado para atualizar.");
            return "redirect:/admin/produtos";
        }

        Produto produtoExistente = produtoExistenteOpt.get();
        produtoExistente.setNome(produtoDto.getNome());
        produtoExistente.setDescricao(produtoDto.getDescricao());
        produtoExistente.setPreco(produtoDto.getPreco());
        produtoExistente.setCategoria(produtoDto.getCategoria());
        
        MultipartFile imagemFile = produtoDto.getImagemFile();
        if (imagemFile != null && !imagemFile.isEmpty()) {
            produtoExistente.setImagem(imagemFile.getBytes());
            produtoExistente.setImagemTipo(imagemFile.getContentType());
        }

        produtoRepository.save(produtoExistente);
        redirectAttributes.addFlashAttribute("successMessage", "Produto '" + produtoExistente.getNome() + "' atualizado com sucesso!");
        return "redirect:/admin/produtos";
    }

    // <<< CORREÇÃO AQUI: ID alterado de Long para String >>>
    @PostMapping("/produtos/remover/{id}")
    public String removerProduto(@PathVariable String id, RedirectAttributes redirectAttributes) {
        if (!produtoRepository.existsById(id)) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erro: Produto não encontrado para remover.");
        } else {
            produtoRepository.deleteById(id);
            redirectAttributes.addFlashAttribute("successMessage", "Produto removido com sucesso!");
        }
        return "redirect:/admin/produtos";
    }

    // ==========================================================
    // --- GERENCIAMENTO DE USUÁRIOS (JÁ ESTAVA CORRETO COM STRING) ---
    // ==========================================================
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
    
    // ==========================================================
    // --- GERENCIAMENTO DE PEDIDOS ---
    // ==========================================================
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