package Services.impl;

import Constants.Actions;
import Constants.Constant;
import CustomExceptions.ExceptionDB;
import Logging.LogCrashHandler;
import Services.DBServices;
import Services.MetaServices;
import Helper.ReaderWriter;
import entity.Columns;
import entity.DBResponse;
import entity.Table;
import entity.WhereCondition;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static Constants.Constant.DELIMITER;
import static Constants.Constant.DELIMITER_SPLIT;

public class DBImplementation implements DBServices {
    public DBImplementation(){
        metaServ = new MetaImplementation();
    }


    @Override
    public DBResponse DBcreate(String dbName) {
        String dbPathName = Constant.DB_LOCATION +dbName;
        File file = new File(dbPathName);
        if(file.exists()) {
            ReaderWriter.printMesages("DB already exist");
            LogCrashHandler.insertLogCrash("createDatabase : Database already exists with name : "+dbName);
            return new DBResponse(false,"DB already exist");
        }
        boolean  create = file.mkdir();
        ReaderWriter.printMesages("DB created successfully");
        return new DBResponse(create,"DB created successfully");
    }

    private final MetaServices metaServ;




    @Override
    public DBResponse DBuse(String dbName) {
        boolean have = new File(Constant.DB_LOCATION +dbName).exists();
        if(have) {
            Constant.CURRENT_DB = dbName;
            ReaderWriter.printMesages(dbName + "DB selected");
            System.out.println();
            return new DBResponse(true,dbName + "DB selected");
        } else {
            ReaderWriter.printMesages("DB not exist");
            LogCrashHandler.insertLogCrash("useDatabase : Database not exists with name : "+dbName);
            return new DBResponse(false,"DB not exist");
        }
    }



    @Override
    public DBResponse TabInsert(String tableName, Table tableDate) throws IOException, ExceptionDB {
        if(Constant.CURRENT_DB.equals("")) {
            ReaderWriter.printMesages("Please select a database");
            LogCrashHandler.insertLogCrash("insertTable : No database selected");
            return new DBResponse(false,"Please select a database");
        }

        String tabPath = Constant.DB_LOCATION + Constant.CURRENT_DB+"/"+tableName;

        File file = new File(tabPath);
        StringBuilder insSB = new StringBuilder();
        if(!file.exists()) {
            ReaderWriter.printMesages("Table doesn't exist");
            LogCrashHandler.insertLogCrash("insertTable : Table not exists with name : "+tableName);
            return new DBResponse(false,"Table doesn't exist");
        }
        FileWriter colnWriter = new FileWriter(tabPath, true);

        ArrayList<String[]> rowVals = tableDate.getData();
        for (String[] row: rowVals) {
            for (String rowV : row) {
                insSB.append(rowV).append(DELIMITER);
            }
            insSB.append("\n");
        }

        colnWriter.write(insSB.toString());
        colnWriter.close();
        ReaderWriter.printMesages("Data Inserted successfully");
        return new DBResponse(true,"Data Inserted successfully"+tableName);
    }

    @Override
    public DBResponse Tabcreate(String tabN, List<Columns> columns) throws IOException {
        if(Constant.CURRENT_DB.equals("")) {
            ReaderWriter.printMesages("Please select a database");
            LogCrashHandler.insertLogCrash("createTable : No database selected");
            return new DBResponse(false,"Please select a database");
        }
        String tablePath = Constant.DB_LOCATION + Constant.CURRENT_DB+"/"+tabN;
        File file = new File(tablePath);
        if(file.exists()) {
            ReaderWriter.printMesages("Table exists");
            return new DBResponse(false,"Table already exists");
        }
        try {
            file.createNewFile();
        } catch (IOException e) {
            LogCrashHandler.insertLogCrash("createTable : Error Creating Table with name : "+tabN);
            e.printStackTrace();
        }
        FileWriter colNWrite = new FileWriter(tablePath, true);
        StringBuilder sb = new StringBuilder();
        for(Columns columnsName : columns) {
            sb.append(columnsName.getColName()).append(DELIMITER);
        }
        colNWrite.write(sb.toString() + "\n");
        colNWrite.close();
        //Insert Meta Table
        Table table = new Table();
        table.setRow(new String[]{Constant.CURRENT_DB,tabN});
        metaServ.TabInsertMeta(table);

        //Insert Meta Columns
        table = new Table();
        for (Columns col:columns) {
            table.setRow(new String[]{
                    tabN,
                    col.getColName(),
                    col.getValTypes().toString(),
                    col.getConstraint() == null ? "" : String.join(",", col.getConstraint())});
        }
        metaServ.ColInsertMeta(table);
        ReaderWriter.printMesages("Created");
        return new DBResponse(true,"Created "+tabN);
    }

    @Override
    public DBResponse TabUpdate(String tableName, String column, String value, WhereCondition whereCon) throws IOException, ExceptionDB {
        if (Constant.CURRENT_DB.equals("")) {
            ReaderWriter.printMesages("Please select a database");
            LogCrashHandler.insertLogCrash("updateTable : No database selected");
            return new DBResponse(false, "Please select a database");
        }

        String tabPath = Constant.DB_LOCATION + Constant.CURRENT_DB + "/" + tableName;
        String newTabPath = Constant.DB_LOCATION + Constant.CURRENT_DB + "/" + "temp" + tableName;

        File file = new File(tabPath);
        if (!file.exists()) {
            ReaderWriter.printMesages("Table doesn't exist");
            LogCrashHandler.insertLogCrash("updateTable : Table not exists with name :"+tableName);
            return new DBResponse(false, "Table doesn't exist");
        }

        File tFile = new File(newTabPath);
        tFile.createNewFile();

        BufferedWriter upWrite = new BufferedWriter(new FileWriter(newTabPath));
        BufferedReader upRead = new BufferedReader(new FileReader(tabPath));


        String colidx = upRead.readLine();
        boolean wrote = false;
        int secondCOlCOunt = 0;
        int idxCount = 0;
        int secondIdxCount = 0;
        int colCount = 0;

        String[] arrcol = colidx.split(DELIMITER_SPLIT);
        if ((whereCon.getActions().equals(Actions.GREATER_THAN)) || (whereCon.getActions().equals(Actions.LESS_THAN))) {
            for (String col : arrcol) {
                if (col.contentEquals(whereCon.getCol())) {
                    secondCOlCOunt = secondIdxCount;
                }
                secondIdxCount += 1;
            }

            for (String col : arrcol) {
                if (col.contentEquals(column)) {
                    colCount = idxCount;
                }
                idxCount  = idxCount + 1;
            }


        } else {
            for (String col : arrcol) {
                if (col.contentEquals(column)) {
                    colCount = idxCount;
                }
                idxCount  = idxCount + 1;
            }
        }

        String upReader = null;

        if (whereCon.getActions().equals(Actions.LESS_THAN)) {
            while ((upReader = upRead.readLine()) != null) {
                String[] greaterThanLineParts = upReader.split(DELIMITER_SPLIT);
                if (Integer.parseInt(greaterThanLineParts[secondCOlCOunt]) < Integer.parseInt(whereCon.getValue())) {
                    String[] toChange = upReader.split(DELIMITER_SPLIT);
                    System.out.println(upReader);
                    toChange[colCount] = value;
                    upReader = "";
                    for (String rename : toChange) {
                        upReader = upReader + rename + DELIMITER;
                    }
                    System.out.println(upReader);
                }
                if (!wrote) {
                    upWrite.write(colidx + "\n");
                    wrote = true;
                }
                upWrite.write(upReader + "\n");
            }
        }else
        if (whereCon.getActions().equals(Actions.GREATER_THAN)) {
            while ((upReader = upRead.readLine()) != null) {
                String[] greaterThanLineParts = upReader.split(DELIMITER_SPLIT);
                if (Integer.parseInt(greaterThanLineParts[secondCOlCOunt]) > Integer.parseInt(whereCon.getValue())) {
                    String[] toChnage = upReader.split(DELIMITER_SPLIT);
                    System.out.println(upReader);
                    toChnage[colCount] = value;
                    upReader = "";
                    for (String rename : toChnage) {
                        upReader = upReader + rename + DELIMITER;
                    }
                    System.out.println(upReader);
                }

                if (!wrote) {
                    upWrite.write(colidx + "\n");
                    wrote = true;
                }
                upWrite.write(upReader + "\n");
            }
        }  else {
            while ((upReader = upRead.readLine()) != null) {

                if (upReader.contains(whereCon.getValue())) {
                    String[] tochange = upReader.split(DELIMITER_SPLIT);
                    System.out.println(upReader);
                    tochange[colCount] = value;
                    upReader = "";
                    for (String rename : tochange) {
                        upReader = upReader + rename + DELIMITER;
                    }
                    System.out.println(upReader);
                }
                if (!wrote) {
                    upWrite.write(colidx + "\n");
                    wrote = true;
                }
                upWrite.write(upReader + "\n");
            }
        }
        upWrite.close();
        upRead.close();

        file.delete();
        tFile.renameTo(file);
        return new DBResponse(true,"Selected successfully"+tableName);
    }



    @Override
    public DBResponse TabDrop(String tableName) {
        if(Constant.CURRENT_DB.equals("")) {
            LogCrashHandler.insertLogCrash("dropTable : No database selected");
            ReaderWriter.printMesages("DB NOT SELECTED");

            return new DBResponse(false,"Database not selected");
        }
        boolean fileExist = new File(Constant.DB_LOCATION + Constant.CURRENT_DB+"/"+tableName).exists();
        if(fileExist) {
            File file = new File(Constant.DB_LOCATION + Constant.CURRENT_DB + "/" + tableName);
            Boolean success = file.delete();

            if (success) {
                metaServ.TabDrop(tableName);
                ReaderWriter.printMesages("success");

                return new DBResponse(true, tableName + " has been deleted successfully");
            }
        }
        LogCrashHandler.insertLogCrash("dropTable : Error deleted table : "+tableName);
        return new DBResponse(false, tableName + " Error deleting");
    }

    @Override
    public DBResponse TabSelect(String tableName, String columns, WhereCondition whre) throws IOException, ExceptionDB {
        if(Constant.CURRENT_DB.equals("")) {
            ReaderWriter.printMesages("Please select a database");
            LogCrashHandler.insertLogCrash("dropTable : No database selected");
            return new DBResponse(false,"Please select a database");
        }
        int greateThanIdx  = 0;
        int lesscolIdx  = 0;
        int greateCol = 0;

        int lessCol = 0;

        String tabLoc = Constant.DB_LOCATION + Constant.CURRENT_DB+"/"+tableName;

        File file = new File(tabLoc);
        if(!file.exists()) {
            ReaderWriter.printMesages("Table doesn't exist");
            LogCrashHandler.insertLogCrash("dropTable : No table exists with name : "+tableName);
            return new DBResponse(false,"Table doesn't exist");
        }

            if (whre != null) {
                if (whre.getActions().equals(Actions.GREATER_THAN)) {
                    String whereval = whre.getValue();
                    String whereCol = whre.getCol();
                    BufferedReader colreader = new BufferedReader(new FileReader(tabLoc));
                    String header = colreader.readLine();
                    String[] headercols = header.split(DELIMITER_SPLIT);
                    colreader.close();

                    for (String cols: headercols) {
                        if (cols.contentEquals(whereCol)) {
                            greateThanIdx = greateCol;
                        }
                        greateCol += 1;
                    }
                    BufferedReader reader1 = new BufferedReader(new FileReader(tabLoc));
                    reader1.readLine();
                    String select = null;
                    while ((select = reader1.readLine()) != null) {
                        String[] greater = select.split(DELIMITER_SPLIT);
                        if (Integer.parseInt(greater[greateThanIdx]) > Integer.parseInt(whereval)) {
                            System.out.println(select);
                        }
                    }
                    reader1.close();
                } else if (whre.getActions().equals(Actions.LESS_THAN)) {
                    String whereval = whre.getValue();
                    String whereCol = whre.getCol();
                    BufferedReader reader1 = new BufferedReader(new FileReader(tabLoc));
                    String header = reader1.readLine();
                    String[] headerCol = header.split(DELIMITER_SPLIT);
                    reader1.close();
                    for (String column: headerCol) {
                        if (column.contentEquals(whereCol)) {
                            lesscolIdx = lessCol;
                        }
                        lessCol += 1;
                    }
                    BufferedReader reader2 = new BufferedReader(new FileReader(tabLoc));
                    // skip reading first line
                    reader2.readLine();
                    String selectRead = null;
                    while ((selectRead = reader2.readLine()) != null) {
                        String[] greater = selectRead.split(DELIMITER_SPLIT);
                        if (Integer.parseInt(greater[lesscolIdx]) < Integer.parseInt(whereval)) {
                            System.out.println(selectRead);
                        }
                    }
                    reader2.close();
                } else {
                    String val = whre.getValue();
                    String col = whre.getCol();
                    BufferedReader reader1 = new BufferedReader(new FileReader(tabLoc));
                    String readerReadLine = null;
                    while ((readerReadLine = reader1.readLine()) != null) {
                        if (readerReadLine.contains(val)) {
                            System.out.println(readerReadLine);
                        }
                    }
                    reader1.close();
                }
            } else {
                BufferedReader reader1 = new BufferedReader(new FileReader(tabLoc));
                String readerString = null;
                while((readerString=reader1.readLine())!=null) {
                    System.out.println(readerString);
                }
                reader1.close();
            }

        return new DBResponse(true,"Selected successfully"+tableName);
    }

    @Override
    public DBResponse TabDelete(String tableName, WhereCondition whereCondition) throws IOException, ExceptionDB {
        if(Constant.CURRENT_DB.equals("")) {
            ReaderWriter.printMesages("Please select a database");
            LogCrashHandler.insertLogCrash("deleteTable : No database selected");
            return new DBResponse(false,"Please select a database");
        }

        String tabLoc = Constant.DB_LOCATION + Constant.CURRENT_DB+"/"+tableName;
        String newTabLoc = Constant.DB_LOCATION + Constant.CURRENT_DB+"/"+"temp"+tableName;

        File file = new File(tabLoc);
        if(!file.exists()) {
            ReaderWriter.printMesages("Table doesn't exist");
            LogCrashHandler.insertLogCrash("deleteTable : No table exists with name : "+tableName);
            return new DBResponse(false,"Table doesn't exist");
        }

        File tempFile = new File(newTabLoc);
        tempFile.createNewFile(); // create file

        String values = whereCondition.getValue();
        String column = whereCondition.getCol();

        BufferedReader delReader = new BufferedReader(new FileReader(tabLoc));
        BufferedWriter delWriter = new BufferedWriter(new FileWriter(newTabLoc));
        String delLine = null;

        while((delLine=delReader.readLine())!=null) {
            if(!delLine.contains(values)) {
                delWriter.write(delLine + "\n");
            }
        }

        delReader.close();
        delWriter.close();

        file.delete();
        tempFile.renameTo(file);

        ReaderWriter.printMesages("Deleted row");
        return new DBResponse(true,"Inserted successfully"+tableName);
    }
}
