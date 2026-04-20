package main.java.model;
import java.util.ArrayList;
import java.util.List;

public class Carrinho {
    private long id;
    private Double valorTotal;
    private static long proximoId = 1;


    private List<ItemCarrinho> itens = new ArrayList<>();

    public void adicionarItem(ItemCarrinho item) {
        itens.add(item);
        item.setCarrinho(this); // mesma lógica do pedido
    }

    public void removerItem(ItemCarrinho item) {
        if(itens.remove(item)){
            item.setCarrinho(null);}
    }




    public double calcularTotal() {
        double total = 0;
        for (ItemCarrinho item : itens) {
            total = total + item.getSubtotal();
        }
        return total;
    }


    public long getId() {
        return id;
    }

    public void setValorTotal(double valorTotal) {
        this.valorTotal = valorTotal;

    }
    public double getValorTotal() {
        return valorTotal;
    }

    public Carrinho() {
        this.id = proximoId;
        proximoId++;
    }

}
