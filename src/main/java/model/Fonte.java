package main.java.model;

public class Fonte extends Componente{
    private int potenciaWatts;
    private String certificacao;

    public Fonte(String nome, String marca, double preco, double peso, int estoque, int consumoWatts, int potenciaWatts, String certificacao){
        super(nome, marca, preco, peso, estoque, consumoWatts);
        this.potenciaWatts = potenciaWatts;
        this.certificacao = certificacao;
    }
    public Fonte(){
        super();
    }
    public int getPotenciaWatts() {
        return potenciaWatts;
    }
    public void setPotenciaWatts(int potenciaWatts) {
        this.potenciaWatts = potenciaWatts;
    }
    public String getCertificacao() {
        return certificacao;
    }
    public void setCertificacao(String certificacao) {
        this.certificacao = certificacao;
    }
}