package main.java.model;

public class Gabinete extends Componente {
    private String formatoPlacaMaeSuportado;
    private int comprimentoMaxGpuMM;
    public Gabinete(String nome, String marca, double preco, double peso, int estoque, int consumoWatts, String formatoPlacaMaeSuportado, int comprimentoMaxGpuMM) {
        super(nome, marca, preco, peso, estoque, consumoWatts);
        this.formatoPlacaMaeSuportado = formatoPlacaMaeSuportado;
        this.comprimentoMaxGpuMM = comprimentoMaxGpuMM;
    }
    public Gabinete(){
        super();
    }
    public String getFormatoPlacaMaeSuportado() {
        return formatoPlacaMaeSuportado;
    }
    public void setFormatoPlacaMaeSuportado(String formatoPlacaMaeSuportado) {
        this.formatoPlacaMaeSuportado = formatoPlacaMaeSuportado;
    }
    public int getComprimentoMaxGpuMM() {
        return comprimentoMaxGpuMM;
    }
    public void setComprimentoMaxGpuMM(int comprimentoMaxGpuMM) {
        this.comprimentoMaxGpuMM = comprimentoMaxGpuMM;
    }
}