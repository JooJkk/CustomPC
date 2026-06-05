package exception;

public class ClienteJaExisteException extends RuntimeException {
    public ClienteJaExisteException() {
        super("Cliente já existe");
    }
}
