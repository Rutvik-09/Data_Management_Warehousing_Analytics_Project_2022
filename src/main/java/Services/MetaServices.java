package Services;

import CustomExceptions.ExceptionDB;
import entity.DBResponse;
import entity.Table;

import java.util.List;

public interface MetaServices {
    // remove the entry from meta data when dropping the table
    DBResponse TabDrop(String name);
    // insert column details in meta data
    DBResponse ColInsertMeta(Table name);

    // get tables names from specific db
    List<String> TablesInDBsGet(String name) throws ExceptionDB;

    // insert table details in meta data
    DBResponse TabInsertMeta(Table name);

    // Get database from Metadata for use in General Logs
    List<String> DBsGet() throws ExceptionDB;

}
