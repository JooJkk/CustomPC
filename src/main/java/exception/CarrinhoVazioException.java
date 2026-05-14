package main.java.exception;

public class CarrinhoVazioException extends Exception {
    public CarrinhoVazioException() {
        super("o seu carrinho está vazio!! adicione algo para poder finalizar a compra.");
    }
        public CarrinhoVazioException(String mensagem) {
        super(mensagem);
    }
}
