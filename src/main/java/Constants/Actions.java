package Constants;

// For where conditions
public enum Actions {

    GREATER_THAN(">"),

    LESS_THAN("<"),

    NOT_EQUALS("!="),

    EQUALS("=");


    Actions(String action){
        this.action = action;
    }

    private final String action;
}
