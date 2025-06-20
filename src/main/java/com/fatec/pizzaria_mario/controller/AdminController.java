package com.fatec.pizzaria_mario.controller;

import com.fatec.pizzaria_mario.domain.Produto;
import com.fatec.pizzaria_mario.dto.ProdutoDTO; // Importe o novo DTO
import com.fatec.pizzaria_mario.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private ProdutoRepository produtoRepository;

    // MÉTODO ATUALIZADO: Agora envia um DTO vazio para o formulário
    @GetMapping("/produtos")
    public String gerenciarProdutos(Model model) {
        List<Produto> produtos = produtoRepository.findAll();
        model.addAttribute("produtos", produtos);
        model.addAttribute("produtoDto", new ProdutoDTO()); // Para o formulário de cadastro
        return "admin-produtos";
    }

    // MÉTODO ATUALIZADO: Recebe o DTO em vez de vários @RequestParam
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

    // O resto dos métodos continua igual...
    @GetMapping("/produtos/editar/{id}")
    public String mostrarFormularioEdicao(@PathVariable String id, Model model) {
        // (A lógica de edição também deveria usar um DTO, mas faremos isso depois para não complicar)
        Optional<Produto> produtoOpt = produtoRepository.findById(id);
        if (produtoOpt.isEmpty()) {
            return "redirect:/admin/produtos";
        }
        model.addAttribute("produto", produtoOpt.get()); 
        return "admin-produto-editar";
    }

    @PostMapping("/produtos/editar/{id}")
    public String editarProduto(@PathVariable String id, Produto produto, @RequestParam("imagemFile") MultipartFile imagemFile) throws IOException {
        // ... (Lógica de edição continua a mesma por enquanto)
        Optional<Produto> produtoOpt = produtoRepository.findById(id);
        if (produtoOpt.isEmpty()) { return "redirect:/admin/produtos"; }
        Produto produtoExistente = produtoOpt.get();
        produtoExistente.setNome(produto.getNome());
        produtoExistente.setDescricao(produto.getDescricao());
        produtoExistente.setPreco(produto.getPreco());
        produtoExistente.setCategoria(produto.getCategoria());
        if (!imagemFile.isEmpty()) {
            produtoExistente.setImagem(imagemFile.getBytes());
            produtoExistente.setImagemTipo(imagemFile.getContentType());
        }
        produtoRepository.save(produtoExistente);
        return "redirect:/admin/produtos";
    }

    @PostMapping("/produtos/{id}/remover")
    public String removerProduto(@PathVariable String id) {
        produtoRepository.deleteById(id);
        return "redirect:/admin/produtos";
    }
}