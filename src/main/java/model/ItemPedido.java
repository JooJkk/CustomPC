package main.java.model;
public class ItemPedido {
    private int quantidade;
    private Double precoUnitario;

    private Componente componente;

    public void setComponente(Componente componente) {
        this.componente = componente;
    }

    public Componente getComponente() {
        return componente;
    }
    ItemPedido(){

    }

    private Pedido pedido;

    public double getSubtotal() {
        if(precoUnitario == null)
            return 0;
        return quantidade * precoUnitario;
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

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }
    public Pedido getPedido() {
        return pedido;
    }



}
