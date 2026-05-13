package main.java.exception;

public class ComponenteNotFoundException extends RuntimeException {
    public ComponenteNotFoundException(String message) {
        super(message);
    }
}
