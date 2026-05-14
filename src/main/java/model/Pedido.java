package main.java.model;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.time.LocalDateTime;

public class Pedido {
    private int id;
    private LocalDateTime data;
    private String status;
    private double valorTotal;
    private Pagamento pagamento;
    private final List<ItemPedido> itens = new ArrayList<>();

    public Pedido() {
        this.data = LocalDateTime.now();
        this.status = "AGUARDANDO_PAGAMENTO";
    }

    public void adicionarItem(ItemPedido item) {
        if (item == null) return;

        itens.add(item);
        item.setPedido(this);
        atualizarTotalInterno();
    }

    private void atualizarTotalInterno() {
        double total = 0;
        for (ItemPedido item : itens) {
            total += item.getSubtotal();
        }
        this.valorTotal = total;
    }

    public List<ItemPedido> getItens() {
        return Collections.unmodifiableList(itens);
    }

    public void setPagamento(Pagamento pagamento) {
        if (this.pagamento != null) {
            throw new IllegalStateException("Este pedido já possui um pagamento associado.");
        }
        this.pagamento = pagamento;
        pagamento.setPedido(this);
    }


    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public LocalDateTime getData() { return data; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public double getValorTotal() { return valorTotal; }

    public Pagamento getPagamento() { return pagamento; }
}