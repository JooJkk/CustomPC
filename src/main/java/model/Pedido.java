package main.java.model;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.time.LocalDateTime;

public class Pedido {
    private long id;
    private LocalDateTime data;
    private String status;
    private double valorTotal;
    private Pagamento pagamento;
    private final List<ItemPedido> itens = new ArrayList<>();
    private static long proximoId = 1;
    public Pedido() {
        this.data = LocalDateTime.now();
        this.status = "AGUARDANDO_PAGAMENTO";
        this.id = proximoId;
        proximoId++;
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


    public long getId() { return id; }

    public LocalDateTime getData() { return data; }

    public String getStatus() { return status; }
    public void setStatus(String status) {
        if(status == null || status.isBlank()){
            throw new IllegalArgumentException("status não pode ser nulo");
        }
        this.status = status; }

    public double getValorTotal() { return valorTotal; }

    public Pagamento getPagamento() { return pagamento; }
}