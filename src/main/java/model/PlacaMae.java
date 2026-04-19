package main.java.model;

public class PlacaMae extends Componente{
    private String socket;
    private String tipoRamSuportada;
    private int slotsRam;
    private String formato;
    public PlacaMae(String nome, String marca, double preco, double peso, int estoque, int consumoWatts, String socket, int slotsRam, String tipoRamSuportada, String formato){
        super(nome, marca, preco, peso, estoque, consumoWatts);
        this.formato = formato;
        this.slotsRam = slotsRam;
        this.socket = socket;
        this.tipoRamSuportada = tipoRamSuportada;
    }
    public PlacaMae(){
        super();
    }
    public void setSocket(String socket) {
        this.socket = socket;
    }
    public void setFormato(String formato) {
        this.formato = formato;
    }
    public void setSlotsRam(int slotsRam) {
        this.slotsRam = slotsRam;
    }
    public void setTipoRamSuportada(String tipoRamSuportada) {
        this.tipoRamSuportada = tipoRamSuportada;
    }

    public String getSocket() {
        return socket;
    }
    public int getSlotsRam() {
        return slotsRam;
    }
    public String getTipoRamSuportada() {
        return tipoRamSuportada;
    }
    public String getFormato() {
        return formato;
    }
}
