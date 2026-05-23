package main.java.model.componentes;

public class PlacaMae extends Componente{
    private String socket;
    private String tipoRamSuportada;
    private int slotsRam;
    private String formato;
    public PlacaMae(String nome, String marca, double preco, double peso, int estoque, int consumoWatts, String socket, int slotsRam, String tipoRamSuportada, String formato){
        super(nome, marca, preco, peso, estoque, consumoWatts);
        setFormato(formato);
        setSlotsRam(slotsRam);
        setSocket(socket);
        setTipoRamSuportada(tipoRamSuportada);
    }
    public PlacaMae(){
        super();
    }
    public void setSocket(String socket) {
        if(socket == null || socket.isBlank()){
            throw new IllegalArgumentException("socket inválido");
        }
        this.socket = socket;
    }
    public void setFormato(String formato) {
        if(formato == null || formato.isBlank()){
            throw new IllegalArgumentException("formato inválido");
        }

        this.formato = formato;
    }
    public void setSlotsRam(int slotsRam) {
        if(slotsRam <= 0){
            throw new IllegalArgumentException("slots RAM inválidos");
        }
        this.slotsRam = slotsRam;
    }
    public void setTipoRamSuportada(String tipoRamSuportada) {
        if(tipoRamSuportada == null || tipoRamSuportada.isBlank()){
            throw new IllegalArgumentException("tipo de RAM inválido");
        }
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
