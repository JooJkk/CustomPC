package main.java.model;

public class CupomDesconto {
    private String codigo;
    private double percentual;
    private double valorMinimo;
    private boolean ativo;

    public CupomDesconto(String codigo,double percentual,double valorMinimo,boolean ativo){
        this.codigo = codigo;
        this.percentual = percentual;
        this.valorMinimo = valorMinimo;
        this.ativo = ativo;
    }
    public String getCodigo() {
        return codigo;
    }
    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }
    public double getPercentual() {
        return percentual;
    }
    public void setPercentual(double percentual) {
        this.percentual = percentual;
    }
    public double getValorMinimo() {
        return valorMinimo;
    }
    public void setValorMinimo(double valorMinimo) {
        this.valorMinimo = valorMinimo;
    }
    public boolean getAtivo() {
        return ativo;
    }
    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }
}
