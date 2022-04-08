package Services;

import CustomExceptions.DBException;
import entity.Column;
import entity.DBResponse;
import entity.Table;
import entity.WhereCondition;

import java.io.IOException;
import java.util.List;

public interface DatabaseServices {
    // create Database
    DBResponse createDatabase(String dbName);

    // use Database
    DBResponse useDatabase(String dbName);

    // create Table
    DBResponse createTable(String tableName, List<Column> columns) throws IOException, DBException;

    // insert into table
    DBResponse insertTable(String tableName, Table tableDate) throws IOException, DBException;

    // select data from table
    DBResponse selectTable(String tableName, String columns, WhereCondition whereCondition) throws IOException, DBException;

    // update table data
    DBResponse updateTable(String tableName, String column, String value, WhereCondition whereCondition) throws IOException, DBException;

    // delete table
    DBResponse deleteTable(String tableName, WhereCondition whereCondition) throws IOException, DBException;

    // drop table
    DBResponse dropTable(String tableName);

}
