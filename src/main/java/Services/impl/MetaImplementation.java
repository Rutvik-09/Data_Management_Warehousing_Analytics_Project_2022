package Services.impl;

import CustomExceptions.ExceptionDB;
import Logging.LogCrashHandler;
import Services.MetaServices;
import entity.DBResponse;
import entity.Table;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static Constants.Constant.*;
import static Constants.Constant.ENDOFLINE;

public class MetaImplementation implements MetaServices {

    @Override
    public List<String> DBsGet() throws ExceptionDB {
        File tabData = new File(DB_LOCATION_STABLE + META_DATA_DIRECTORY + SLASH + TABMETA);
        if (!tabData.isFile()) {
            LogCrashHandler.insertLogCrash("getDbs : Meta data not exists");
            throw new ExceptionDB("Metadata not exists");
        }
        List<String> dbs = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(tabData))) {
            String temp;
            int i =0;
            while ((temp = reader.readLine()) != null) {
                if(i++==0)continue;
                dbs.add(temp.split(DELIMITER_SPLIT)[0]);
            }
        } catch (IOException e) {
            LogCrashHandler.insertLogCrash("getDBs : "+e.getMessage());
            e.printStackTrace();
        }
        return dbs.stream().distinct().collect(Collectors.toList());
    }


    @Override
    public DBResponse TabInsertMeta(Table table) {
        return getDbResponse(table, TABMETA);
    }

    @Override
    public List<String> TablesInDBsGet(String dbName) throws ExceptionDB {
        File tableDetails = new File(DB_LOCATION_STABLE + META_DATA_DIRECTORY + SLASH + TABMETA);
        if (!tableDetails.isFile()) {
            LogCrashHandler.insertLogCrash("getTablesInDBs : Meta data not exists");
            throw new ExceptionDB("Metadata not exists");
        }
        List<String> tables = new ArrayList<>();
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(tableDetails))) {
            String temp;
            while ((temp = bufferedReader.readLine()) != null) {
                String[] tempArr = temp.split(DELIMITER_SPLIT);
                if (tempArr[0].equalsIgnoreCase(dbName)) {
                    tables.add(tempArr[1]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            LogCrashHandler.insertLogCrash("getTablesInDBs : "+ e.getMessage());
        }
        return tables;
    }

    private DBResponse getDbResponse(Table table, String columnMeta) {
        File columnDetailsTable = new File(DB_LOCATION_STABLE + META_DATA_DIRECTORY + SLASH + columnMeta);
        if (!columnDetailsTable.isFile()) {
            LogCrashHandler.insertLogCrash("getDbResponse : Meta data not exists");
            return new DBResponse(false, "Metadata not exists");
        }
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(columnDetailsTable, true))) {
            for (String[] row : table.getData()) {
                bufferedWriter.append(String.join(DELIMITER, row)).append(ENDOFLINE);
            }
            bufferedWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
            LogCrashHandler.insertLogCrash("getDbResponse : "+e.getMessage());
            return new DBResponse(false, "Table entry failed");
        }
        return new DBResponse(true, "Success");
    }



    @Override
    public DBResponse ColInsertMeta(Table table) {
        return getDbResponse(table, COLMETA);
    }


    @Override
    public DBResponse TabDrop(String tableName) {
        File columnDetailsTable = new File(DB_LOCATION_STABLE + META_DATA_DIRECTORY + SLASH + COLMETA);
        if (!columnDetailsTable.isFile()) {
            LogCrashHandler.insertLogCrash("dropTable : Meta data not exists");
            return new DBResponse(false, "Metadata not exists");
        }
        StringBuilder finalValues = new StringBuilder();
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(columnDetailsTable))) {
            String temp;
            while ((temp = bufferedReader.readLine()) != null) {
                String[] tempArr = temp.split(DELIMITER_SPLIT);
                if (!tempArr[0].equalsIgnoreCase(tableName)) {
                    finalValues.append(temp).append(ENDOFLINE);
                }
            }
        } catch (IOException e) {
            LogCrashHandler.insertLogCrash("dropTable : "+e.getMessage());
            return new DBResponse(false, "failed creating entry");
        }
        PrintingData(columnDetailsTable, finalValues);

        File tableDetails = new File(DB_LOCATION_STABLE + META_DATA_DIRECTORY + SLASH + TABMETA);
        if (!tableDetails.isFile()) {
            LogCrashHandler.insertLogCrash("dropTable : Meta file not found");
            return new DBResponse(false, "Meta file not found. Create meta first");
        }
        StringBuilder finalValuesTable = new StringBuilder();
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(tableDetails))) {
            String temp;
            while ((temp = bufferedReader.readLine()) != null) {
                String[] tempArr = temp.split(DELIMITER_SPLIT);
                if (!tempArr[1].equalsIgnoreCase(tableName)) {
                    finalValuesTable.append(temp).append(ENDOFLINE);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            LogCrashHandler.insertLogCrash("dropTable : "+e.getMessage());
            return new DBResponse(false, "Table entry failed");
        }
        PrintingData(tableDetails, finalValuesTable);
        return new DBResponse(true, "Success");
    }

    private void PrintingData(File tableDetails, StringBuilder finalValuesTable) {
        try(PrintWriter printWriter = new PrintWriter(tableDetails)){
            printWriter.print("");
        } catch (FileNotFoundException e) {
            LogCrashHandler.insertLogCrash("PrintingData : "+e.getMessage());
        }
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(tableDetails))) {
            bufferedWriter.write(finalValuesTable.toString());
            bufferedWriter.flush();
        } catch (IOException e) {
            LogCrashHandler.insertLogCrash("PrintingData : "+e.getMessage());
        }
    }
}
