package Services.impl;

import Constants.Datatype;
import CustomExceptions.DBException;
import Services.MetadataServices;
import entity.Column;
import entity.DBResponse;
import entity.Table;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static Constants.QueryConstants.*;
import static Constants.QueryConstants.ENDOFLINE;

public class MetadataServicesImpl implements MetadataServices {

    @Override
    public List<String> getDBs() throws DBException {
        File tabData = new File(DB_PATH_PERMANENT + META_DATA_DIRECTORY + SLASH + TABLE_META);
        if (!tabData.isFile()) {
            throw new DBException("Metadata not exists");
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
            e.printStackTrace();
        }
        return dbs.stream().distinct().collect(Collectors.toList());
    }

    @Override
    public List<String> getTablesInDBs(String dbName) throws DBException {
        File tableDetails = new File(DB_PATH_PERMANENT + META_DATA_DIRECTORY + SLASH + TABLE_META);
        if (!tableDetails.isFile()) {
            throw new DBException("Metadata not exists");
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
        }
        return tables;
    }

    @Override
    public DBResponse insertTablesDataInMetaData(Table table) {
        return getDbResponse(table, TABLE_META);
    }

    @Override
    public DBResponse insertColumnsDataInMetaData(Table table) {
        return getDbResponse(table, COLUMN_META);
    }

    private DBResponse getDbResponse(Table table, String columnMeta) {
        File columnDetailsTable = new File(DB_PATH_PERMANENT + META_DATA_DIRECTORY + SLASH + columnMeta);
        if (!columnDetailsTable.isFile()) {
            return new DBResponse(false, "Metadata not exists");
        }
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(columnDetailsTable, true))) {
            for (String[] row : table.getData()) {
                bufferedWriter.append(String.join(DELIMITER, row)).append(ENDOFLINE);
            }
            bufferedWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
            return new DBResponse(false, "Table entry failed");
        }
        return new DBResponse(true, "Success");
    }

    @Override
    public DBResponse dropTable(String tableName) {
        File columnDetailsTable = new File(DB_PATH_PERMANENT + META_DATA_DIRECTORY + SLASH + COLUMN_META);
        if (!columnDetailsTable.isFile()) {
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
            e.printStackTrace();
            return new DBResponse(false, "failed creating entry");
        }
        PrintingData(columnDetailsTable, finalValues);

        File tableDetails = new File(DB_PATH_PERMANENT + META_DATA_DIRECTORY + SLASH + TABLE_META);
        if (!tableDetails.isFile()) {
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
            return new DBResponse(false, "Table entry failed");
        }
        PrintingData(tableDetails, finalValuesTable);
        return new DBResponse(true, "Success");
    }

    private void PrintingData(File tableDetails, StringBuilder finalValuesTable) {
        try(PrintWriter writer = new PrintWriter(tableDetails)){
            writer.print("");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(tableDetails))) {
            bufferedWriter.write(finalValuesTable.toString());
            bufferedWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
