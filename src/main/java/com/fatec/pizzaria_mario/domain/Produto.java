package com.fatec.pizzaria_mario.domain;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.math.BigDecimal;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Data
@Document(collection = "produtos")
public class Produto {

    @Id
    private String id;
    @NotBlank(message = "O nome é obrigatório")
    private String nome;
    @NotBlank(message = "A descrição é obrigatória")
    private String descricao;
    @NotNull(message = "O preço é obrigatório")
    @Positive(message = "O preço deve ser positivo")
    private BigDecimal preco;
    @NotBlank(message = "A categoria é obrigatória")
    private String categoria;
    private boolean disponibilidade;
    
    // CAMPOS DE IMAGEM ADICIONADOS (substituindo o imageUrl)
    private byte[] imagem;
    private String imagemTipo; // Ex: "image/jpeg", "image/png"
}