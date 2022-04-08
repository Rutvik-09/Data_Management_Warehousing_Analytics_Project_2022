package Services;

import CustomExceptions.DatabaseException;

import java.util.List;

public interface MetadataServices {
    public List<String> getDatabases() throws DatabaseException;
    public List<String> getTables(String dbName) throws DatabaseException;
}
