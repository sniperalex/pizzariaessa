package com.fatec.pizzaria_mario.repository;

import com.fatec.pizzaria_mario.domain.Pedido;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface PedidoRepository extends MongoRepository<Pedido, String> {
    
    // MÉTODO NOVO para buscar o histórico de pedidos de um usuário
    List<Pedido> findByUsuarioIdOrderByDataHoraDesc(String usuarioId);
}