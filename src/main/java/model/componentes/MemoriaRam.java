package main.java.model.componentes;

public class MemoriaRam extends Componente {
    private String tipoRam;
    private int capacidadeGB;
    private int frequenciaMHz;
    public MemoriaRam(String nome, String marca, double preco, double peso, int estoque, int consumoWatts, String tipoRam, int capacidadeGB, int frequenciaMHz){
        super(nome, marca, preco, peso, estoque, consumoWatts);
        setCapacidadeGB(capacidadeGB);
        setTipoRam(tipoRam);
        setFrequenciaMHz(frequenciaMHz);
    }
    public MemoriaRam(){
        super();
    }
    public void setCapacidadeGB(int capacidadeGB) {
        if(capacidadeGB < 0){
            throw new IllegalArgumentException("capacidade não pode ser negativa");
        }
        this.capacidadeGB = capacidadeGB;
    }
    public void setFrequenciaMHz(int frequenciaMHz) {
        if(frequenciaMHz < 0){
            throw new IllegalArgumentException("frequência não pode ser negativa");
        }
        this.frequenciaMHz = frequenciaMHz;
    }
    public void setTipoRam(String tipoRam) {
        if(tipoRam == null || tipoRam.isBlank()){
            throw new IllegalArgumentException("tipo de ram não pode ser nula");
        }
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
