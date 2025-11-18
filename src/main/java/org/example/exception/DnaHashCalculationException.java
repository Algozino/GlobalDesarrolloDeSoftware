package org.example.exception;

// excepcion personalizada que pide la guía
public class DnaHashCalculationException extends RuntimeException {
    public DnaHashCalculationException(String message, Throwable cause) {
        super(message, cause);
    }
}
