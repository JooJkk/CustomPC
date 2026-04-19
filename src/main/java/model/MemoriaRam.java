package main.java.model;

public class MemoriaRam extends Componente{
    private String tipoRam;
    private int capacidadeGB;
    private int frequenciaMHz;
    public MemoriaRam(String nome, String marca, double preco, double peso, int estoque, int consumoWatts, String tipoRam, int capacidadeGB, int frequenciaMHz){
        super(nome, marca, preco, peso, estoque, consumoWatts);
        this.capacidadeGB = capacidadeGB;
        this.tipoRam = tipoRam;
        this.frequenciaMHz = frequenciaMHz;
    }
    public MemoriaRam(){
        super();
    }
    public void setCapacidadeGB(int capacidadeGB) {
        this.capacidadeGB = capacidadeGB;
    }
    public void setFrequenciaMHz(int frequenciaMHz) {
        this.frequenciaMHz = frequenciaMHz;
    }
    public void setTipoRam(String tipoRam) {
        this.tipoRam = tipoRam;
    }

    public int getCapacidadeGB() {
        return capacidadeGB;
    }
    public String getTipoRam() {
        return tipoRam;
    }
    public int getFrequenciaMHz() {
        return frequenciaMHz;
    }
}
