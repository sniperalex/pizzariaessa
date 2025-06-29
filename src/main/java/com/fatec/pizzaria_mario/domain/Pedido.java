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
    
    // CAMPOS ATUALIZADOS PARA LIGAÇÃO COM O USUÁRIO
    private String usuarioId;
    private String nomeCliente;
    private String enderecoEntrega;

    private List<ItemPedido> itens;
    private StatusPedido status;
    private LocalDateTime dataHora;
    private BigDecimal total;
    private String formaPagamento; 
    private String observacao; // Observações do cliente
    private Boolean precisaTroco;
    private java.math.BigDecimal valorTroco;
    private String origemPedido; // app, telefone, balcao
}