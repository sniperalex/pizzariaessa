package com.fatec.pizzaria_mario.domain;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.math.BigDecimal;

@Data
@Document(collection = "produtos")
public class Produto {

    @Id
    private String id;
    private String nome;
    private String descricao;
    private BigDecimal preco;
    private String categoria;
    private boolean disponibilidade;
    
    // CAMPOS DE IMAGEM ADICIONADOS (substituindo o imageUrl)
    private byte[] imagem;
    private String imagemTipo; // Ex: "image/jpeg", "image/png"
}