package CustomExceptions;

public class DuplicateUser extends Exception{
    public DuplicateUser(String errorMessage) {
        super(errorMessage);
    }
}
