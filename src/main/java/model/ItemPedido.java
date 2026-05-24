package model;

import model.componentes.Componente;

public class ItemPedido {

    private int quantidade;
    private double precoUnitario;

    private Pedido pedido;
    private Componente componente;

    public ItemPedido() {

    }

    public double getSubtotal() {

        return quantidade * precoUnitario;
    }

    public void setQuantidade(int quantidade) {

        if(quantidade <= 0){
            throw new IllegalArgumentException(
                    "quantidade deve ser maior que zero"
            );
        }

        this.quantidade = quantidade;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setPrecoUnitario(double precoUnitario) {

        if(precoUnitario < 0){
            throw new IllegalArgumentException(
                    "preço unitário não pode ser negativo"
            );
        }

        this.precoUnitario = precoUnitario;
    }

    public double getPrecoUnitario() {
        return precoUnitario;
    }

    public void setPedido(Pedido pedido) {

        if(pedido == null){
            throw new IllegalArgumentException(
                    "pedido não pode ser nulo"
            );
        }

        this.pedido = pedido;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public void setComponente(Componente componente) {

        if(componente == null){
            throw new IllegalArgumentException(
                    "componente não pode ser nulo"
            );
        }

        this.componente = componente;
        this.precoUnitario = componente.getPreco();
    }
    public Componente getComponente() {
        return componente;
    }
}
