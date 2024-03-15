package io.github.KarMiguel.parkingapi.exception;

public class CodeUniqueViolationException extends RuntimeException {
    public CodeUniqueViolationException(String message) {
        super(message);
    }
}
