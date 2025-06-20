package com.fatec.pizzaria_mario.domain;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Document(collection = "pedidos")
public class Pedido {

    @Id
    private String id;
    private List<ItemPedido> itens;
    private Cliente cliente;
    private StatusPedido status;
    private LocalDateTime dataHora;
    private BigDecimal total;
    private String formaPagamento; // "credito", "debito", "dinheiro", "pix"
}