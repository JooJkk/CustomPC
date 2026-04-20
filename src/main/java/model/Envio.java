package main.java.model;

public class Envio {
    private String codigoRastreio;
    private double valorFrete;
    private String statusEntrega;

    public Envio(String codigoRastreio, double valorFrete, String statusEntrega) {
        this.codigoRastreio = codigoRastreio;
        this.valorFrete = valorFrete;
        this.statusEntrega = statusEntrega;

    }
    public Envio(){

    }


    public String getCodigoRastreio() {return codigoRastreio;}

    public void setCodigoRastreio(String codigoRastreio) {
        this.codigoRastreio = codigoRastreio;
    }

    public double getValorFrete() {
        return valorFrete;
    }

    public void setValorFrete(double valorFrete) {
        this.valorFrete = valorFrete;
    }

    public String getStatusEntrega() {
        return statusEntrega;
    }

    public void setStatusEntrega(String statusEntrega) {
        this.statusEntrega = statusEntrega;
    }

}