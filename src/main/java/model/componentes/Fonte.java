package main.java.model.componentes;

public class Fonte extends Componente{
    private int potenciaWatts;
    private String certificacao;

    public Fonte(String nome, String marca, double preco, double peso, int estoque, int consumoWatts, int potenciaWatts, String certificacao){
        super(nome, marca, preco, peso, estoque, consumoWatts);
        setPotenciaWatts(potenciaWatts);
        setCertificacao(certificacao);
    }
    public Fonte(){
        super();
    }
    public int getPotenciaWatts() {
        return potenciaWatts;
    }
    public void setPotenciaWatts(int potenciaWatts) {
        if(potenciaWatts < 0){
            throw new IllegalArgumentException("potencia não pode ser negativa");
        }
        this.potenciaWatts = potenciaWatts;
    }
    public String getCertificacao() {
        return certificacao;
    }
    public void setCertificacao(String certificacao) {
        if(certificacao == null || certificacao.isBlank()){
            throw new IllegalArgumentException("certificação não pode ser nula");

        }
        this.certificacao = certificacao;
    }
}