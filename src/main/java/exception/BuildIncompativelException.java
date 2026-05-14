package main.java.exception;

public class BuildIncompativelException extends RuntimeException {
    public BuildIncompativelException(String mensagem) {
        super(mensagem);
    }
}