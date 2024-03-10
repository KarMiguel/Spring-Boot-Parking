package io.github.KarMiguel.parkingapi.exception;

public class EntityUserNotFoundException extends RuntimeException {
    public EntityUserNotFoundException(String message) {
        super(message);
    }
}
