package com.fatec.pizzaria_mario.repository;

import com.fatec.pizzaria_mario.domain.Pedido;
import com.fatec.pizzaria_mario.domain.StatusPedido;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public interface PedidoRepository extends MongoRepository<Pedido, String> {
    
    List<Pedido> findByUsuarioIdOrderByDataHoraDesc(String usuarioId);
    
    List<Pedido> findByDataHoraBetweenAndStatusIn(LocalDateTime start, LocalDateTime end, Collection<StatusPedido> statuses);
    
    @Query("{ 'nomeCliente': { $regex: ?0, $options: 'i' } }")
    List<Pedido> findByNomeClienteContaining(String nomeCliente);

    long countByStatusAndDataHoraBetween(StatusPedido status, LocalDateTime start, LocalDateTime end);
}