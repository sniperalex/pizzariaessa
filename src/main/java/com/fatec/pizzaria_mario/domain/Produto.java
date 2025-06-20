package com.fatec.pizzaria_mario.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.math.BigDecimal;

@Getter
@Setter
@Document(collection = "produtos")
public class Produto {

    @Id
    private String id;

    private String nome;

    private String descricao;

    private BigDecimal preco;

    private String categoria; // Ex: "PIZZA_SALGADA", "PIZZA_DOCE", "BEBIDA", "ESFIHA"

    private boolean disponibilidade = true; // Por padrão, um novo produto está disponível

}