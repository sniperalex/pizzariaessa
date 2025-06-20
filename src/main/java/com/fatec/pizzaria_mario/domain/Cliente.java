package com.fatec.pizzaria_mario.domain;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "clientes")
public class Cliente {
    @Id
    private String id;
    private String nome;
    private String telefone;
    private String endereco; // Simplificado por enquanto
}