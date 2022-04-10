package CustomExceptions;

public class ExceptionDB extends RuntimeException {
    public ExceptionDB(String call){
        super(call);
    }
}
