package shagiev.carwash.service.exceptions;

public class NoSuchIdException extends RuntimeException {

    public NoSuchIdException() {
        super("No such id");
    }

    public NoSuchIdException(String message) {
        super(message);
    }
}
