package Constants;

// Datatype class will handle all datatypes
public enum Datatype {
    INT("int"),
    BOOL("boolean"),
    DOUBLE("double"),
    VARCHAR("string");

    public final String type;
    Datatype(String type){
        this.type = type;
    }
}
