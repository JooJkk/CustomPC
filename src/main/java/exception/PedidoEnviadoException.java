package main.java.exception;

public class PedidoEnviadoException extends Exception  {

public PedidoEnviadoException() {
    super("esse pedido já saiu para a entrega e não será possível cancelá-lo");
}
    public PedidoEnviadoException(String mensagem) {
        super(mensagem);
    }



}
