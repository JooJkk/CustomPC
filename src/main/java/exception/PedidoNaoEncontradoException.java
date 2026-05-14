package main.java.exception;

public class PedidoNaoEncontradoException extends Exception {
    public PedidoNaoEncontradoException() {
        super("O pedido com o ID informado não existe no sistema.");
    }
    public PedidoNaoEncontradoException(String mensagem) {
        super(mensagem);
    }


}
