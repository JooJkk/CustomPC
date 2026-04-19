package main.java.model;

import java.time.LocalDate;

public class OrdemMontagem {
    private long id;
    private String status;
    private LocalDate dataCriacao;
    private static long proximoId = 1;
    public OrdemMontagem() {
        this.id = proximoId;
        proximoId++;
    }

    public long getId() {return id;}

    public String getStatus() {return status;}
    public void setStatus(String status) {this.status = status;}

    public LocalDate getDataCriacao() {return dataCriacao;}
    public void setDataCriacao(LocalDate dataCriacao) {this.dataCriacao = dataCriacao;}
}