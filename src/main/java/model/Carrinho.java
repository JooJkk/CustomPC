package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

public class Carrinho {
    private int id;
    private double valorTotal= 0.0;
    private static int proximoId = 1;

    public Carrinho() {
        this.id = proximoId++;
    }

    private List<ItemCarrinho> itens = new ArrayList<>();

    public void adicionarItem(ItemCarrinho item) {
        itens.add(item);
        item.setCarrinho(this);// mesma lógica do pedido
        atualizarTotalInterno();
    }

    public void removerItem(ItemCarrinho item) {
        if (itens.remove(item)) {
            item.setCarrinho(null); //faz com q o item q saia do carrinho nao seja mais apontado.
            atualizarTotalInterno();
        }
    }

    // tirei calcaulartotal e coloquei esse. esse metodo eh disparado automaticamente dentro de adicionaritem e removeritem
    private void atualizarTotalInterno() {
        double total = 0;
        for (ItemCarrinho item : itens) {
            total += item.getSubtotal();
        }
        this.valorTotal = total;
    }

    //caso o cliente queira comprar novamente, o carrinho da compra passada vai estar zerado
    public void limpar() {
        this.itens.clear();
        this.valorTotal = 0.0;
    }

    public List<ItemCarrinho> getItens() {
        return Collections.unmodifiableList(itens);  //precisa passar pelo metodo adicionaritem antes pra n baguncar a logica.impede que as pessoas deem clear, remove, set...
    }

    public int getId() {
        return id;
    }

    public double getValorTotal() {
        return valorTotal;
    }
}