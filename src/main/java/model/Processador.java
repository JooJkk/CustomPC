package main.java.model;

public class Processador extends Componente{
    private String socket;
    private int tdp;
    public Processador(String nome, String marca, double preco, double peso, int estoque, int consumoWatts, String socket, int tdp){
        super(nome, marca, preco, peso, estoque, consumoWatts);
        this.socket = socket;
        this.tdp = tdp;
    }

    public void setSocket(String socket) {
        this.socket = socket;
    }
    public void setTdp(int tdp) {
        this.tdp = tdp;
    }

    public int getTdp() {
        return tdp;
    }
    public String getSocket() {
        return socket;
    }
}
