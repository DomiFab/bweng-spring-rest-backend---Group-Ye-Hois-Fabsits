package at.technikum.springrestbackend.exception;

public class UserAlreadyJoinedException extends RuntimeException{
    public UserAlreadyJoinedException(String message){
        super(message);
    }
}
