import java.util.ArrayList;
import java.util.List;

public class Carrinho {
    private long idcar;
    private Double valorTotal;



    private List<ItemCarrinho> itens = new ArrayList<>();

    public void adicionarItem(ItemCarrinho item) {
        itens.add(item);
        item.setCarrinho(this); // mesma lógica do pedido
    }
/*
    public void removerItem(ItemCarrinho Item) {
        itens.remove(item);
        item.setCarrinho(null);
    }

 */


    public double calcularTotal() {
        double total = 0;
        for (ItemCarrinho item : itens) {
            total = total + item.getSubtotal();
        }
        return total;
    }





    public void setIdcar(long idcar) {
        this.idcar = idcar;

    }
    public long getIdcar() {

        return idcar;
    }

    public void setValorTotal(double valorTotal) {
        this.valorTotal = valorTotal;

    }
    public double getValorTotal() {
        return valorTotal;
    }

    public Carrinho() {

    }

}
