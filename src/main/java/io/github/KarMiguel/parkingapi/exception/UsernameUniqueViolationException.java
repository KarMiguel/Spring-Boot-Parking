package io.github.KarMiguel.parkingapi.exception;

public class UsernameUniqueViolationException extends  RuntimeException {
    public UsernameUniqueViolationException(String message) {
        super(message);
    }
}
