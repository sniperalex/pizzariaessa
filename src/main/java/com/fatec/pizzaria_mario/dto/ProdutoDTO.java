package com.fatec.pizzaria_mario.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;
import java.math.BigDecimal;

@Data
public class ProdutoDTO {
    private String nome;
    private String descricao;
    private BigDecimal preco;
    private String categoria;
    // O arquivo do upload virá aqui
    private MultipartFile imagemFile;
}