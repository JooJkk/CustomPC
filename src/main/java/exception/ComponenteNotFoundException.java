package exception;

public class ComponenteNotFoundException extends RuntimeException {
    public ComponenteNotFoundException(String message) {
        super(message);
    }
}
