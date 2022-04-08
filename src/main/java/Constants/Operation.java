package Constants;

// For where conditions
public enum Operation {
    EQUALS("="),
    LESS_THAN("<"),
    GREATER_THAN(">"),
    NOT_EQUALS("!=");

    private final String operation;
    Operation(String operation){
        this.operation = operation;
    }
}
