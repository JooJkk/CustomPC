package main.java.model;

public abstract class Componente {
    private long id;
    private String nome;
    private String marca;
    private double preco;
    private double peso;
    private int estoque;
    private int consumoWatts;
    private static long proximoId = 1;
    protected  Componente(){
        this.id = proximoId;
        proximoId++;
    }
    protected Componente(String nome, String marca, double preco, double peso, int estoque, int consumoWatts){
        this.id = proximoId;
        proximoId++;
        this.nome = nome;
        this.preco = preco;
        this.estoque = estoque;
        this.marca = marca;
        this.peso = peso;
        this.consumoWatts = consumoWatts;
    }

    public String getNome() {
        return nome;
    }
    public double getPreco() {
        return preco;
    }
    public double getPeso() {
        return peso;
    }
    public int getConsumoWatts() {
        return consumoWatts;
    }
    public int getEstoque() {
        return estoque;
    }
    public long getId() {
        return id;
    }
    public String getMarca() {
        return marca;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
    public void setPreco(double preco) {
        this.preco = preco;
    }
    public void setConsumoWatts(int consumoWatts) {
        this.consumoWatts = consumoWatts;
    }
    public void setEstoque(int estoque) {
        this.estoque = estoque;
    }
    public void setMarca(String marca) {
        this.marca = marca;
    }
    public void setPeso(double peso) {
        this.peso = peso;
    }

}
