package Services;

import CustomExceptions.DBException;
import entity.Column;
import entity.DBResponse;
import entity.Table;

import java.util.List;

public interface MetadataServices {
    // Get database from Metadata for use in General Logs
    List<String> getDBs() throws DBException;
    // get tables names from specific db
    List<String> getTablesInDBs(String db) throws DBException;
    // insert column details in meta data
    DBResponse insertColumnsDataInMetaData(Table table);
    // insert table details in meta data
    DBResponse insertTablesDataInMetaData(Table table);
    // remove the entry from meta data when dropping the table
    DBResponse dropTable(String tableName);
}
