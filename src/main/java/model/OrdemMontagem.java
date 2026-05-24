package model;

import java.time.LocalDate;

public class OrdemMontagem {
    private long id;
    private Pedido pedido;
    private String status;
    private LocalDate dataCriacao;
    private static long proximoId = 1;
    public OrdemMontagem() {
        this.id = proximoId;
        proximoId++;
    }

    public long getId() {return id;}

    public void setPedido(Pedido pedido) {
        if(pedido != null) {
            this.pedido = pedido;
        }
    }

    public Pedido getPedido() {
        return pedido;
    }

    public String getStatus() {return status;}
    public void setStatus(String status) {
        if(status == null || status.isBlank()){
            throw new IllegalArgumentException("status não pode ser nulo");
        }
        this.status = status;}

    public LocalDate getDataCriacao() {return dataCriacao;}
    public void setDataCriacao(LocalDate dataCriacao) {this.dataCriacao = dataCriacao;}
}