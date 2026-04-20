package main.java.model;
import java.util.ArrayList;
import java.util.List;

public class Pedido {
    private long id;
    private int data;
    private String status;
    private Double valorTotal;

    Pedido(){

    }

    private Pagamento pagamento;
    public Pagamento getPagamento() {
        return pagamento;
    }
    public void setPagamento(Pagamento pagamento) {
        this.pagamento = pagamento;
        pagamento.setPedido(this); // eh tipo esse pagamento pertence a este pedido. o pagamento que eu vou receber pertence a esse pedido(setpedido(this))
    }





    private List<ItemPedido> itens = new ArrayList<>();

    public void adicionarItem(ItemPedido item) {

        itens.add(item);
        item.setPedido(this);
    }

    public List<ItemPedido> getItens() {
        return itens;
    }

    public double calcularTotal() {
        double total = 0;

        for (ItemPedido item : itens) {
            total += item.getSubtotal();
        }
        this.valorTotal = total;
        return total;
    }






    public void setId(long id) {
        this.id = id;

    }

    public long getId() {

        return id;
    }

    public void setData(int data) {
        this.data = data;
    }

    public int getData() {
        return data;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus(){
        return status;
    }

    public void setValorTotal(Double valorTotal) {
        this.valorTotal = valorTotal;
    }

    public Double getValorTotal(){
        return valorTotal;
    }



}







