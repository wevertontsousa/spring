package br.com.wevertontsousa.spring.error;

public class InvalidResourceException extends RuntimeException {

    public InvalidResourceException() {
        this("Recurso inválido");
    }

    public InvalidResourceException(String message) {
        super(message);
    }

}
