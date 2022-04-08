package Constants;

public class RegexConstants {
    public static final String INSERT_QUERY= "(\\s*(insert)\\s+(into)\\s+)([\\w-]+)(\\s+(values)\\s*)(\\(\\s*([\\w,-\\.\\\\\"])*\\s*)(\\);)";
    public static final String UPDATE_QUERY = "(\\s*update table\\s+)(([\\w-]+)*\\s+)(set\\s+)(([\\w-]+))\\s+(=)\\s+([\\w\\\"\\.-]+)\\s+(where\\s+)(([\\w\\\"-]+))\\s+(=)\\s+([\\w-]+);)";
    public static final String START_TRANSACTION = "(\\s*start\\s+transaction\\s*;)";
    public static final String CREATE_DATABASE = "(\\s*create\\s+database\\s+)([\\w-]+)*;";
    public static final String CREATE_TABLE= "(\\s*create\\s+table\\s+([\\w-]+)\\s*)(\\(([\\w-]+)*\\s+)(int|varchar\\([0-9]+\\))((\\s*,\\s*([\\w-]+)*\\s+(int|varchar\\([0-9]+\\)))?)*(\\);)";
    public static final String DELETE_QUERY = "(\\s*delete\\s+from\\s+)([\\w-]+\\s+)(where\\s+)([\\w-]+)(\\s+=\\s+)([\\w]+);";
    public static final String USE_DATABASE  ="(\\s+use\\s+database\\s+)([\\w-]+);";


}

