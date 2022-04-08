package Services.impl;

import CustomExceptions.DatabaseException;
import Services.MetadataServices;

import java.util.List;

public class MetadataServicesImpl implements MetadataServices {
    @Override
    public List<String> getDatabases() throws DatabaseException {
        return null;
    }

    @Override
    public List<String> getTables(String dbName) throws DatabaseException {
        return null;
    }
}
