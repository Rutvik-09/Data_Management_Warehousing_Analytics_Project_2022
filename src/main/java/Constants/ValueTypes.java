package Constants;

// ValueTypes class will handle all datatypes
public enum ValueTypes {
    VARCHAR("string"),
    DOUBLE("double"),
    INT("int"),

    BOOL("boolean");

    public final String type;
    ValueTypes(String type){
        this.type = type;
    }
}
