package model;

public class Envio {
    private String codigoRastreio;
    private double valorFrete;
    private String statusEntrega;
    private Endereco endereco;


    public Envio(String codigoRastreio, double valorFrete, String statusEntrega, Endereco endereco) {
        setCodigoRastreio(codigoRastreio);
        setValorFrete(valorFrete);
        setStatusEntrega(statusEntrega);
        setEndereco(endereco);
    }

    public void setEndereco(Endereco endereco) {
        if (endereco == null) {
            throw new IllegalArgumentException("Endereço não pode ser nulo");
        }
        this.endereco = endereco;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public String getCodigoRastreio() {return codigoRastreio;}

    public void setCodigoRastreio(String codigoRastreio) {
        if(codigoRastreio == null || codigoRastreio.isBlank()){
            throw new IllegalArgumentException("código de rastreio inválido");
        }

        this.codigoRastreio = codigoRastreio;
    }

    public double getValorFrete() {
        return valorFrete;
    }

    public void setValorFrete(double valorFrete) {
        if(valorFrete < 0){
            throw new IllegalArgumentException("valor do frete inválido");
        }
        this.valorFrete = valorFrete;
    }

    public String getStatusEntrega() {
        return statusEntrega;
    }

    public void setStatusEntrega(String statusEntrega) {
        if(statusEntrega == null || statusEntrega.isBlank()){
            throw new IllegalArgumentException("status de entrega inválido");
        }

        this.statusEntrega = statusEntrega;
    }

}