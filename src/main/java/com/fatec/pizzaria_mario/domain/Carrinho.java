package com.fatec.pizzaria_mario.domain;

import lombok.Data;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Data
public class Carrinho {

    private List<ItemPedido> itens = new ArrayList<>();
    private BigDecimal total = BigDecimal.ZERO;

    // MÉTODO ATUALIZADO: Agora ele soma a quantidade se o item já existe.
    public void adicionarItem(Produto produto) {
        // Procura se já existe um item com o mesmo produto no carrinho
        Optional<ItemPedido> itemExistente = this.itens.stream()
                .filter(item -> item.getProduto().getId().equals(produto.getId()))
                .findFirst();
        
        if (itemExistente.isPresent()) {
            // Se já existe, apenas incrementa a quantidade
            ItemPedido item = itemExistente.get();
            item.setQuantidade(item.getQuantidade() + 1);
        } else {
            // Se não existe, adiciona o novo item com quantidade 1
            this.itens.add(new ItemPedido(produto, 1));
        }
        recalcularTotal(); // Recalcula o total após qualquer alteração
    }

    // NOVO MÉTODO: Remove um item completamente do carrinho
    public void removerItem(String produtoId) {
        this.itens.removeIf(item -> item.getProduto().getId().equals(produtoId));
        recalcularTotal();
    }

    // NOVO MÉTODO: Atualiza a quantidade de um item específico
    public void atualizarQuantidade(String produtoId, int novaQuantidade) {
        Optional<ItemPedido> itemOpt = this.itens.stream()
                .filter(item -> item.getProduto().getId().equals(produtoId))
                .findFirst();

        if (itemOpt.isPresent()) {
            if (novaQuantidade > 0) {
                // Se a nova quantidade é positiva, atualiza
                itemOpt.get().setQuantidade(novaQuantidade);
            } else {
                // Se a quantidade for zero ou menor, remove o item
                removerItem(produtoId);
            }
        }
        recalcularTotal();
    }

    // Recalcula o valor total do carrinho
    private void recalcularTotal() {
        this.total = itens.stream()
                .map(item -> {
                    BigDecimal precoDoItem = item.getProduto().getPreco();
                    BigDecimal quantidadeDoItem = new BigDecimal(item.getQuantidade());
                    return precoDoItem.multiply(quantidadeDoItem);
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}