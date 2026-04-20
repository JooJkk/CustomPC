package main.java.model;
public class ItemCarrinho {
    private int quantidade;
    private Double precoUnitario;


    private Carrinho carrinho;
    private Componente componente;

    public void setComponente(Componente componente) {
        this.componente = componente;
    }

    public Componente getComponente() {
        return componente;
    }

    public double getSubtotal() {
        if (precoUnitario == null)
            return 0;
        return quantidade * precoUnitario;
    }

    public void setCarrinho(Carrinho carrinho) {
        this.carrinho = carrinho;
    }

    public Carrinho getCarrinho() {
        return carrinho;
    }




    public void setQuantidade(int quantidade){
        this.quantidade = quantidade;
    }
    public int getQuantidade() {
        return quantidade;
    }

    public void setPrecoUnitario(Double precoUnitario) {
        this.precoUnitario = precoUnitario;
    }
    public Double getPrecoUnitario(){
        return precoUnitario;
    }



}
