package Services.impl;

import Constants.Operation;
import Constants.QueryConstants;
import CustomExceptions.DBException;
import Logging.CrashLogWriter;
import Services.DatabaseServices;
import Services.MetadataServices;
import Helper.ReaderWriter;
import entity.Column;
import entity.DBResponse;
import entity.Table;
import entity.WhereCondition;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static Constants.QueryConstants.DELIMITER;
import static Constants.QueryConstants.DELIMITER_SPLIT;

public class DatabaseServicesImpl implements DatabaseServices {
    private final MetadataServices metadataServices;

    public DatabaseServicesImpl(){
        metadataServices = new MetadataServicesImpl();
    }

    @Override
    public DBResponse createDatabase(String dbName) {
        String dbPath = QueryConstants.DB_PATH +dbName;
        File file = new File(dbPath);
        if(file.exists()) {
            ReaderWriter.print("DB already exist");
            CrashLogWriter.addCrashLog("createDatabase : Database already exists with name : "+dbName);
            return new DBResponse(false,"DB already exist");
        }
        boolean  isCreated = file.mkdir();
        ReaderWriter.print("DB created successfully");
        return new DBResponse(isCreated,"DB created successfully");
    }

    @Override
    public DBResponse useDatabase(String dbName) {
        boolean fileExist = new File(QueryConstants.DB_PATH+dbName).exists();
        if(fileExist) {
            QueryConstants.CURRENT_DB = dbName;
            ReaderWriter.print(dbName + "DB selected");
            System.out.println();
            return new DBResponse(true,dbName + "DB selected");
        } else {
            ReaderWriter.print("DB not exist");
            CrashLogWriter.addCrashLog("useDatabase : Database not exists with name : "+dbName);
            return new DBResponse(false,"DB not exist");
        }
    }

    @Override
    public DBResponse createTable(String tableName, List<Column> columns) throws IOException {
        if(QueryConstants.CURRENT_DB.equals("")) {
            ReaderWriter.print("Please select a database");
            CrashLogWriter.addCrashLog("createTable : No database selected");
            return new DBResponse(false,"Please select a database");
        }
        String tablePath = QueryConstants.DB_PATH +QueryConstants.CURRENT_DB+"/"+tableName;
        File file = new File(tablePath);
        if(file.exists()) {
            ReaderWriter.print("Table exists");
            return new DBResponse(false,"Table already exists");
        }
        try {
            file.createNewFile();
        } catch (IOException e) {
            CrashLogWriter.addCrashLog("createTable : Error Creating Table with name : "+tableName);
            e.printStackTrace();
        }
        FileWriter columnNameWriter = new FileWriter(tablePath, true);
        StringBuilder sb = new StringBuilder();
        for(Column columnName: columns) {
            sb.append(columnName.getColumnName()).append(DELIMITER);
        }
        columnNameWriter.write(sb.toString() + "\n");
        columnNameWriter.close();
        //Insert Meta Table
        Table table = new Table();
        table.setRow(new String[]{QueryConstants.CURRENT_DB,tableName});
        metadataServices.insertTablesDataInMetaData(table);

        //Insert Meta Columns
        table = new Table();
        for (Column column:columns) {
            table.setRow(new String[]{
                    tableName,
                    column.getColumnName(),
                    column.getDatatype().toString(),
                    column.getConstraints() == null ? "" : String.join(",", column.getConstraints())});
        }
        metadataServices.insertColumnsDataInMetaData(table);
        ReaderWriter.print("Created");
        return new DBResponse(true,"Created "+tableName);
    }

    @Override
    public DBResponse insertTable(String tableName, Table tableDate) throws IOException, DBException {
        if(QueryConstants.CURRENT_DB.equals("")) {
            ReaderWriter.print("Please select a database");
            CrashLogWriter.addCrashLog("insertTable : No database selected");
            return new DBResponse(false,"Please select a database");
        }

        String tablePath = QueryConstants.DB_PATH +QueryConstants.CURRENT_DB+"/"+tableName;

        File file = new File(tablePath);
        if(!file.exists()) {
            ReaderWriter.print("Table doesn't exist");
            CrashLogWriter.addCrashLog("insertTable : Table not exists with name : "+tableName);
            return new DBResponse(false,"Table doesn't exist");
        }
        FileWriter columnNameWriter = new FileWriter(tablePath, true);
        StringBuilder insertValuesStringBuilder = new StringBuilder();

        ArrayList<String[]> rowVals = tableDate.getData();
        for (String[] row: rowVals) {
            for (String rowV : row) {
                insertValuesStringBuilder.append(rowV).append(DELIMITER);
            }
            insertValuesStringBuilder.append("\n");
        }

        columnNameWriter.write(insertValuesStringBuilder.toString());
        columnNameWriter.close();
        ReaderWriter.print("Data Inserted successfully");
        return new DBResponse(true,"Data Inserted successfully"+tableName);
    }

    @Override
    public DBResponse updateTable(String tableName, String column, String value, WhereCondition whereCondition) throws IOException, DBException {
        if (QueryConstants.CURRENT_DB.equals("")) {
            ReaderWriter.print("Please select a database");
            CrashLogWriter.addCrashLog("updateTable : No database selected");
            return new DBResponse(false, "Please select a database");
        }

        String tablePath = QueryConstants.DB_PATH + QueryConstants.CURRENT_DB + "/" + tableName;
        String tempTablePath = QueryConstants.DB_PATH + QueryConstants.CURRENT_DB + "/" + "temp" + tableName;

        File file = new File(tablePath);
        if (!file.exists()) {
            ReaderWriter.print("Table doesn't exist");
            CrashLogWriter.addCrashLog("updateTable : Table not exists with name :"+tableName);
            return new DBResponse(false, "Table doesn't exist");
        }

        File createTempFile = new File(tempTablePath);
        createTempFile.createNewFile(); // create file

        BufferedReader updateReader = new BufferedReader(new FileReader(tablePath));
        BufferedWriter updateWriter = new BufferedWriter(new FileWriter(tempTablePath));

        // to check column index
        String columnIndex = updateReader.readLine();
        boolean isColumnNameWritten = false;
        int columnIndexCounter = 0;
        int columnCounter = 0;
        int columnIndexCounter2 = 0;
        int columnCounter2 = 0;
        String[] columnIndexColumns = columnIndex.split(DELIMITER_SPLIT);
        if ((whereCondition.getOperation().equals(Operation.GREATER_THAN)) || (whereCondition.getOperation().equals(Operation.LESS_THAN))) {
            for (String column2 : columnIndexColumns) {
                if (column2.contentEquals(column)) {
                    columnCounter = columnIndexCounter;
                }
                columnIndexCounter += 1;
            }

            for (String column2 : columnIndexColumns) {
                if (column2.contentEquals(whereCondition.getColumn())) {
                    columnCounter2 = columnIndexCounter2;
                }
                columnIndexCounter2 += 1;
            }
        } else {
            for (String column2 : columnIndexColumns) {
                if (column2.contentEquals(column)) {
                    columnCounter = columnIndexCounter;
                }
                columnIndexCounter += 1;
            }
        }

        String updateFileLineReader = null;

        if (whereCondition.getOperation().equals(Operation.GREATER_THAN)) {
            while ((updateFileLineReader = updateReader.readLine()) != null) {
                String[] greaterThanLineParts = updateFileLineReader.split(DELIMITER_SPLIT);
                if (Integer.parseInt(greaterThanLineParts[columnCounter2]) > Integer.parseInt(whereCondition.getValue())) {
                    String[] targetLineSplitWords = updateFileLineReader.split(DELIMITER_SPLIT);
                    System.out.println(updateFileLineReader);
                    targetLineSplitWords[columnCounter] = value;
                    updateFileLineReader = "";
                    for (String rename : targetLineSplitWords) {
                        updateFileLineReader = updateFileLineReader + rename + DELIMITER;
                    }
                    System.out.println(updateFileLineReader);
                }

                if (!isColumnNameWritten) {
                    updateWriter.write(columnIndex + "\n");
                    isColumnNameWritten = true;
                }
                updateWriter.write(updateFileLineReader + "\n");
            }
        } else if (whereCondition.getOperation().equals(Operation.LESS_THAN)) {
            while ((updateFileLineReader = updateReader.readLine()) != null) {
                String[] greaterThanLineParts = updateFileLineReader.split(DELIMITER_SPLIT);
                if (Integer.parseInt(greaterThanLineParts[columnCounter2]) < Integer.parseInt(whereCondition.getValue())) {
                    String[] targetLineSplitWords = updateFileLineReader.split(DELIMITER_SPLIT);
                    System.out.println(updateFileLineReader);
                    targetLineSplitWords[columnCounter] = value;
                    updateFileLineReader = "";
                    for (String rename : targetLineSplitWords) {
                        updateFileLineReader = updateFileLineReader + rename + DELIMITER;
                    }
                    System.out.println(updateFileLineReader);
                }
                if (!isColumnNameWritten) {
                    updateWriter.write(columnIndex + "\n");
                    isColumnNameWritten = true;
                }
                updateWriter.write(updateFileLineReader + "\n");
            }
        } else {
            while ((updateFileLineReader = updateReader.readLine()) != null) {

                if (updateFileLineReader.contains(whereCondition.getValue())) {
                    String[] targetLineSplitWords = updateFileLineReader.split(DELIMITER_SPLIT);
                    System.out.println(updateFileLineReader);
                    targetLineSplitWords[columnCounter] = value;
                    updateFileLineReader = "";
                    for (String rename : targetLineSplitWords) {
                        updateFileLineReader = updateFileLineReader + rename + DELIMITER;
                    }
                    System.out.println(updateFileLineReader);
                }
                if (!isColumnNameWritten) {
                    updateWriter.write(columnIndex + "\n");
                    isColumnNameWritten = true;
                }
                updateWriter.write(updateFileLineReader + "\n");
            }
        }
        updateWriter.close();
        updateReader.close();

        file.delete();
        createTempFile.renameTo(file);
        return new DBResponse(true,"Selected successfully"+tableName);
    }

    @Override
    public DBResponse deleteTable(String tableName, WhereCondition whereCondition) throws IOException, DBException {
        if(QueryConstants.CURRENT_DB.equals("")) {
            ReaderWriter.print("Please select a database");
            CrashLogWriter.addCrashLog("deleteTable : No database selected");
            return new DBResponse(false,"Please select a database");
        }

        String tablePath = QueryConstants.DB_PATH +QueryConstants.CURRENT_DB+"/"+tableName;
        String tempTablePath = QueryConstants.DB_PATH +QueryConstants.CURRENT_DB+"/"+"temp"+tableName;

        File file = new File(tablePath);
        if(!file.exists()) {
            ReaderWriter.print("Table doesn't exist");
            CrashLogWriter.addCrashLog("deleteTable : No table exists with name : "+tableName);
            return new DBResponse(false,"Table doesn't exist");
        }

        File createTempDeleteFile = new File(tempTablePath);
        createTempDeleteFile.createNewFile(); // create file

        String value = whereCondition.getValue();
        String column = whereCondition.getColumn();

        BufferedReader deleteRowReader = new BufferedReader(new FileReader(tablePath));
        BufferedWriter deleteWriter = new BufferedWriter(new FileWriter(tempTablePath));
        String deleteRowLine = null;

        while((deleteRowLine=deleteRowReader.readLine())!=null) {
            if(!deleteRowLine.contains(value)) {
                deleteWriter.write(deleteRowLine + "\n");
            }
        }

        deleteRowReader.close();
        deleteWriter.close();

        file.delete();
        createTempDeleteFile.renameTo(file);

        ReaderWriter.print("Deleted row");
        return new DBResponse(true,"Inserted successfully"+tableName);
    }

    @Override
    public DBResponse dropTable(String tableName) {
        if(QueryConstants.CURRENT_DB.equals("")) {
            ReaderWriter.print("DB NOT SELECTED");
            CrashLogWriter.addCrashLog("dropTable : No database selected");
            return new DBResponse(false,"Database not selected");
        }
        boolean fileExist = new File(QueryConstants.DB_PATH +QueryConstants.CURRENT_DB+"/"+tableName).exists();
        if(fileExist) {
            File file = new File(QueryConstants.DB_PATH + QueryConstants.CURRENT_DB + "/" + tableName);
            Boolean success = file.delete();

            if (success) {
                ReaderWriter.print("success");
                metadataServices.dropTable(tableName);
                return new DBResponse(true, tableName + " has been deleted successfully");
            }
        }
        CrashLogWriter.addCrashLog("dropTable : Error deleted table : "+tableName);
        return new DBResponse(false, tableName + " Error deleting");
    }

    @Override
    public DBResponse selectTable(String tableName, String columns, WhereCondition whereCondition) throws IOException, DBException {
        if(QueryConstants.CURRENT_DB.equals("")) {
            ReaderWriter.print("Please select a database");
            CrashLogWriter.addCrashLog("dropTable : No database selected");
            return new DBResponse(false,"Please select a database");
        }
        int greaterThanCounterColumn = 0;
        int greaterThanIndexOfColumnUserRequested  = 0;
        int lessThanCounterColumn = 0;
        int lessThanIndexOfColumnUserRequested  = 0;

        String tablePath = QueryConstants.DB_PATH +QueryConstants.CURRENT_DB+"/"+tableName;

        File file = new File(tablePath);
        if(!file.exists()) {
            ReaderWriter.print("Table doesn't exist");
            CrashLogWriter.addCrashLog("dropTable : No table exists with name : "+tableName);
            return new DBResponse(false,"Table doesn't exist");
        }

        if(columns.contentEquals("*")) {
            if (whereCondition != null) {
                if (whereCondition.getOperation().equals(Operation.GREATER_THAN)) {
                    String whereConditionValue = whereCondition.getValue();
                    String whereConditionColumn = whereCondition.getColumn();
                    BufferedReader columnLineReader = new BufferedReader(new FileReader(tablePath));
                    String firstLine = columnLineReader.readLine();
                    String[] columnsFromFirstLine = firstLine.split(DELIMITER_SPLIT);
                    columnLineReader.close();

                    for (String column: columnsFromFirstLine) {
                        if (column.contentEquals(whereConditionColumn)) {
                            greaterThanIndexOfColumnUserRequested = greaterThanCounterColumn;
                        }
                        greaterThanCounterColumn += 1;
                    }
                    BufferedReader selectReader = new BufferedReader(new FileReader(tablePath));
                    // skip reading first line
                    selectReader.readLine();
                    String lineFromSelectReader = null;
                    while ((lineFromSelectReader = selectReader.readLine()) != null) {
                        String[] greaterThanLineParts = lineFromSelectReader.split(DELIMITER_SPLIT);
                        if (Integer.parseInt(greaterThanLineParts[greaterThanIndexOfColumnUserRequested]) > Integer.parseInt(whereConditionValue)) {
                            System.out.println(lineFromSelectReader);
                        }
                    }
                    selectReader.close();
                } else if (whereCondition.getOperation().equals(Operation.LESS_THAN)) {
                    String whereConditionValue = whereCondition.getValue();
                    String whereConditionColumn = whereCondition.getColumn();
                    BufferedReader columnLineReader = new BufferedReader(new FileReader(tablePath));
                    String firstLine = columnLineReader.readLine();
                    String[] columnsFromFirstLine = firstLine.split(DELIMITER_SPLIT);
                    columnLineReader.close();
                    for (String column: columnsFromFirstLine) {
                        if (column.contentEquals(whereConditionColumn)) {
                            lessThanIndexOfColumnUserRequested = lessThanCounterColumn;
                        }
                        lessThanCounterColumn += 1;
                    }
                    BufferedReader selectReader = new BufferedReader(new FileReader(tablePath));
                    // skip reading first line
                    selectReader.readLine();
                    String lineFromSelectReader = null;
                    while ((lineFromSelectReader = selectReader.readLine()) != null) {
                        String[] greaterThanLineParts = lineFromSelectReader.split(DELIMITER_SPLIT);
                        if (Integer.parseInt(greaterThanLineParts[lessThanIndexOfColumnUserRequested]) < Integer.parseInt(whereConditionValue)) {
                            System.out.println(lineFromSelectReader);
                        }
                    }
                    selectReader.close();
                } else {
                    String value = whereCondition.getValue();
                    String column = whereCondition.getColumn();
                    BufferedReader selectReader = new BufferedReader(new FileReader(tablePath));
                    String lineFromSelectReader = null;
                    while ((lineFromSelectReader = selectReader.readLine()) != null) {
                        if (lineFromSelectReader.contains(value)) {
                            //print the entire line
                            System.out.println(lineFromSelectReader);
                        }
                    }
                    selectReader.close();
                }
            } else {
                BufferedReader selectReader = new BufferedReader(new FileReader(tablePath));
                String lineFromSelectReader = null;
                while((lineFromSelectReader=selectReader.readLine())!=null) {
                    System.out.println(lineFromSelectReader);
                }
                selectReader.close();
            }
        }
        return new DBResponse(true,"Selected successfully"+tableName);
    }
}
