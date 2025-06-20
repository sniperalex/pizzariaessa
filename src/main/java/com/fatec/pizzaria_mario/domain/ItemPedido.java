package com.fatec.pizzaria_mario.domain;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemPedido {
    private Produto produto;
    private int quantidade;
}