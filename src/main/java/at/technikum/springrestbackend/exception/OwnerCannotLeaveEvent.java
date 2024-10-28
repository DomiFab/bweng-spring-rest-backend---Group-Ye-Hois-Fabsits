package at.technikum.springrestbackend.exception;

public class OwnerCannotLeaveEvent extends RuntimeException{
    public OwnerCannotLeaveEvent(String message) {
        super(message);
    }
}
