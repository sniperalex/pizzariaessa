package com.fatec.pizzaria_mario.repository;

import com.fatec.pizzaria_mario.domain.Pedido;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PedidoRepository extends MongoRepository<Pedido, String> {
}