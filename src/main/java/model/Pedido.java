package main.java.model;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;

public class Pedido {
    private long id;
    private LocalDate data;
    private String status;
    private Double valorTotal;
    private static long proximoId = 1;
    public Pedido(){
        this.id = proximoId;
        proximoId++;
    }

    private Envio envio;
    public Envio getEnvio() {
        return envio;
    }
    public void setEnvio(Envio envio) {
        this.envio = envio;
    }

    private OrdemMontagem ordemMontagem;
    public OrdemMontagem getOrdemMontagem() {
        return ordemMontagem;
    }
    public void setOrdemMontagem(OrdemMontagem ordemMontagem) {
        this.ordemMontagem = ordemMontagem;
    }

    private CupomDesconto cupomDesconto;
    public CupomDesconto getCupomDesconto() {
        return cupomDesconto;
    }
    public void setCupomDesconto(CupomDesconto cupomDesconto) {
        this.cupomDesconto = cupomDesconto;
    }

    private Pagamento pagamento;
    public Pagamento getPagamento() {
        return pagamento;
    }
    public void setPagamento(Pagamento pagamento) {
        this.pagamento = pagamento;
        if (pagamento != null) {
            pagamento.setPedido(this);} // eh tipo esse pagamento pertence a este pedido. o pagamento que eu vou receber pertence a esse pedido(setpedido(this))
    }

    public boolean cancelar() {
        if (status != null && (status.equalsIgnoreCase("Enviado") || status.equalsIgnoreCase("Entregue"))) {
            return false; //não pode ser cancelado depois de enviado ou entregue
        }
        status = "Cancelado";
        return true;
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



    public long getId() {

        return id;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public LocalDate getData() {
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







