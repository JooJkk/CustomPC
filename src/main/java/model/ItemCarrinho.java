package model;

import model.componentes.Componente;

public class ItemCarrinho {
    private Componente componente;


    private int quantidade;


    private double precoUnitario;

    private Carrinho carrinho;

    public ItemCarrinho() {}

    public ItemCarrinho(Componente componente, int quantidade) {
        setComponente(componente);
        setQuantidade(quantidade);
        this.precoUnitario = componente.getPreco();
    }

    public double getSubtotal() {
        return quantidade * precoUnitario;
    }

    // só aceita números maiores que 0
    public void setQuantidade(int quantidade) {
        if (quantidade <= 0) {
            throw new IllegalArgumentException(
                    "A quantidade deve ser maior que zero."
            );
        }

        this.quantidade = quantidade;
    }

    public Componente getComponente() {
        return componente;
    }

    public void setComponente(Componente componente) {
        if(componente == null){
            throw new IllegalArgumentException("componente não pode ser nulo");
        }
        this.componente = componente;

        if (this.precoUnitario == 0) {
            this.precoUnitario = componente.getPreco();
        }
    }

    public double getPrecoUnitario() {
        return precoUnitario;
    }

    public void setPrecoUnitario(double precoUnitario) {
        if(precoUnitario < 0){
            throw new IllegalArgumentException("preço não pode ser negativo");
        }
        this.precoUnitario = precoUnitario;
    }

    public void setCarrinho(Carrinho carrinho) {
        this.carrinho = carrinho;
    }

    public Carrinho getCarrinho() {
        return carrinho;
    }

    public int getQuantidade() {
        return quantidade;
    }
}