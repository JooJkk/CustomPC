package model.componentes;

public abstract class Componente {
    private long id;
    private String nome;
    private String marca;
    private double preco;
    private double peso;
    private int estoque;
    private int consumoWatts;
    private static long proximoId = 1;
    private int nivelDesempenho;
    protected  Componente(){
        this.id = proximoId;
        proximoId++;
    }
    protected Componente(String nome, String marca, double preco, double peso, int estoque, int consumoWatts){
        this.id = proximoId;
        proximoId++;
        setNome(nome);
        setMarca(marca);
        setPreco(preco);
        setPeso(peso);
        setEstoque(estoque);
        setConsumoWatts(consumoWatts);
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
        if (nome == null || nome.isBlank() ) {
            throw new IllegalArgumentException("nome não pode ser nulo");
        }
        this.nome = nome;
    }
    public void setPreco(double preco) {
        if(preco < 0){
            throw new IllegalArgumentException("preço não pode ser negativo");
        }
        this.preco = preco;
    }
    public void setConsumoWatts(int consumoWatts) {
        if(consumoWatts < 0){
            throw new IllegalArgumentException("O consumo de watts não pode ser negativo");
        }
        this.consumoWatts = consumoWatts;
    }
    public void setEstoque(int estoque) {
        if(estoque < 0){
            throw new IllegalArgumentException("estoque não pode ser negativo");
        }
        this.estoque = estoque;
    }
    public void setMarca(String marca) {
        if(marca == null || marca.isBlank()){
            throw new IllegalArgumentException("marca não pode ser nula");
        }
        this.marca = marca;
    }
    public void setPeso(double peso) {
        if(peso < 0){
            throw new IllegalArgumentException("peso não pode ser negativo");
        }
        this.peso = peso;
    }



    public int getNivelDesempenho() { return nivelDesempenho; }
    public void setNivelDesempenho(int nivelDesempenho) {
        if(nivelDesempenho < 0){
            throw new IllegalArgumentException("nível de desempenho não pode ser negativo");
        }
        this.nivelDesempenho = nivelDesempenho; }
}
