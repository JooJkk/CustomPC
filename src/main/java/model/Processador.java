package main.java.model;

public class Processador extends Componente{
    private String socket;
    private int tdp;
    public Processador(String nome, String marca, double preco, double peso, int estoque, int consumoWatts, String socket, int tdp){
        super(nome, marca, preco, peso, estoque, consumoWatts);
        setSocket(socket);
        setTdp(tdp);
    }
    public Processador(){
        super();
    }
    public void setSocket(String socket) {
        if(socket == null || socket.isBlank()){
            throw new IllegalArgumentException("socket não pode ser nula");
        }
        this.socket = socket;
    }
    public void setTdp(int tdp) {
        if(tdp < 0){
            throw new IllegalArgumentException("tdp não pode ser negativo");
        }
        this.tdp = tdp;
    }

    public int getTdp() {
        return tdp;
    }
    public String getSocket() {
        return socket;
    }
}
