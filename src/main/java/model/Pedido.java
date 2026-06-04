package model;
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
    private List<ItemPedido> itens = new ArrayList<>();
    private Endereco endereco;
    private double frete;
    private OrdemMontagem ordemMontagem;
    private Cliente cliente;
    public Pedido() {
        this.data = LocalDateTime.now();
        this.status = "AGUARDANDO_PAGAMENTO";
    }

    public void setId(long id) {
        this.id = id;
    }
    public double getFrete() {
        return frete;
    }

    public void setFrete(double frete) {
        this.frete = frete;
    }
    public void adicionarItem(ItemPedido item) {
        if (item == null) return;

        itens.add(item);
        atualizarTotalInterno();
    }

    public OrdemMontagem getOrdemMontagem() {
        return ordemMontagem;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public void setOrdemMontagem(OrdemMontagem ordemMontagem) {
        if(ordemMontagem != null) {
            this.ordemMontagem = ordemMontagem;
        }
    }

    private void atualizarTotalInterno() {
        double total = 0;
        for (ItemPedido item : itens) {
            total += item.getSubtotal();
        }
        this.valorTotal = total + frete;
    }

    public List<ItemPedido> getItens() {
        return Collections.unmodifiableList(itens);
    }

    public void setPagamento(Pagamento pagamento) {
        if (this.pagamento != null) {
            throw new IllegalStateException("Este pedido já possui um pagamento associado.");
        }
        this.pagamento = pagamento;

    }


    public long getId() { return id; }

    public void setEndereco(Endereco endereco) {
        if (endereco == null) {
            throw new IllegalArgumentException("Endereço não pode ser nulo");
        }
        this.endereco = endereco;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public LocalDateTime getData() { return data; }

    public String getStatus() { return status; }
    public void setStatus(String status) {
        if(status == null || status.isBlank()){
            throw new IllegalArgumentException("status não pode ser nulo");
        }
        this.status = status; }

    public double getValorTotal() { return valorTotal; }

    public void setValorTotal(double valorTotal) {this.valorTotal = valorTotal;
    }

    public Pagamento getPagamento() { return pagamento; }
}