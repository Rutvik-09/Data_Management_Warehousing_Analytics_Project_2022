package Services.impl;

import Constants.ValueTypes;
import Constants.Actions;
import Constants.Constant;
import CustomExceptions.ExceptionDB;
import Logging.LogEventHandler;
import Logging.LogQueryHandler;
import Services.DBServices;
import Services.QueryService;
import entity.Columns;
import entity.DBResponse;
import entity.Table;
import entity.WhereCondition;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class QueryImplementation implements QueryService {

    @Override
    public void readData() throws IOException, ExceptionDB {
        Scanner scanner =  new Scanner(System.in);
        String queryString;
        do {
            queryString = scanner.nextLine();
            if (!queryString.equals("close;")) {
                try {
                    parseTheQuery(queryString);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        } while(!queryString.equals("close;"));
    }

    private Actions typeOfActionGet(String str){
        if(str.equalsIgnoreCase("=")) return Actions.EQUALS;
        if(str.equalsIgnoreCase("<")) return Actions.LESS_THAN;
        if(str.equalsIgnoreCase(">")) return Actions.GREATER_THAN;
        if(str.equalsIgnoreCase("!=")) return Actions.NOT_EQUALS;
        return null;
    }

    private void parseTheQuery(String queryString) throws IOException, ExceptionDB {
        String query_type = queryString.replaceAll (" .*", "");
        query_type = query_type.toLowerCase ();
        DBServices db= new DBImplementation();
        queryString =queryString.toLowerCase();
        switch(query_type){

                case "use":
                    queryString = queryString.replaceAll(";", "");
                    String[] words1 = queryString.split(" ");
                    String dbName = words1[1];
                    db.DBuse(dbName);
                    Constant.CURRENT_DB = dbName;
                    LogQueryHandler.addQueryLog("Success",queryString);
                    break;
                case "create":
                    queryString = queryString.replaceAll(";$", "");
                    String[] words = queryString.split(" ");
                    if(words[1].equalsIgnoreCase("database")) {
                        db.DBcreate(words[2]);
                        LogEventHandler.insertLogEvent("Database : "+ words[2] +" Created");
                    }
                    else if(words[1].equalsIgnoreCase("table")){
                        String queryMain = "create\\stable\\s(\\w+)\\s";
                        String querySub = "\\(((?:\\s*\\w+\\s+\\w+\\(?[0-9]*\\)?,?)+)\\)";
                        Pattern pattern1 = Pattern.compile(queryMain + querySub);
                        Matcher matcher1 = pattern1.matcher(queryString);
                        List<Columns> listOfCols =new ArrayList();
                        if (matcher1.matches()) {
                            String nameOfTab = matcher1.group(1);
                            String colName = matcher1.group(2);
                            String[] cols = colName.split("\\,");
                            for (int i = 0; i < cols.length; i++) {
                                Columns colsObj =new Columns();
                                String column = cols[i].trim();
                                String columnName = column.substring(0, column.indexOf(' '));
                                colsObj.setColName(columnName);
                                String temp = column.substring(column.indexOf(' ')).trim();
                                System.out.println(temp);
                                String dataType = temp.indexOf(' ') == -1 ? temp : temp.substring(0, temp.indexOf(' '));
                                colsObj.setValTypes(ValueTypes.valueOf(dataType.toUpperCase(Locale.ROOT)));
                                if(temp.indexOf(' ')!=-1) {
                                    String consts = temp.substring(temp.indexOf(' '));
                                    String[] constrs = {consts};
                                    colsObj.setConstraint(constrs);
                                }
                                listOfCols.add(colsObj);
                            }
                            DBResponse DBResponse = db.Tabcreate(nameOfTab, listOfCols);
                            if(!DBResponse.getStatus()){
                                System.out.println("ERROR Occur: "+ DBResponse.getMessage());
                            }
                            else {
                                LogEventHandler.insertLogEvent("Table : "+ nameOfTab +" Created in Database "+ Constant.CURRENT_DB);
                                LogQueryHandler.addQueryLog("Success",queryString);
                            }

                        }
                        else{
                            LogQueryHandler.addQueryLog("Failed",queryString);
                            System.out.println("not a valid queryString");
                        }
                    }
                    break;
                case "select":
                    Pattern pattern3 = Pattern.compile("select\\s+(.*?)\\s*from\\s+(.*?)\\s*(where\\s(.*?)\\s*)?;", Pattern.DOTALL);
                    Matcher matcher3 = pattern3.matcher(queryString);
                    matcher3.find();
                    boolean match = matcher3.matches ();
                    if(match) {
                        String colName = matcher3.group(1);
                        String NameOftab = matcher3.group (2);
                        String where = matcher3.group (4);
                        WhereCondition ConditionForWhere = null;
                        if(where != null) {
                            String[] condition = where.split(" ");
                            ConditionForWhere = new WhereCondition();
                            ConditionForWhere.setCol(condition[0]);
                            ConditionForWhere.setActions(typeOfActionGet(condition[1]));
                            ConditionForWhere.setValue(condition[2].replaceAll("\'", ""));
                        }
                        DBResponse DBResponse = db.TabSelect(NameOftab,colName,ConditionForWhere);
                        if(!DBResponse.getStatus()){
                            System.out.println("ERROR Occur: "+ DBResponse.getMessage());
                        }
                        else {
                            LogQueryHandler.addQueryLog("Success",queryString);
                        }
                    }
                    else {
                        LogQueryHandler.addQueryLog("Failed",queryString);
                        System.out.println("Not a valid Query");
                    }
                    break;
                case "insert":
                    Pattern pattern2 = Pattern.compile("insert into\\s(.*?)\\s(.*?)\\svalues\\s(.*?);", Pattern.DOTALL);
                    Matcher matcher2 = pattern2.matcher(queryString);
                    matcher2.find();
                    if(matcher2.matches()) {
                    	String table = matcher2.group(1);
                        String[] columns = matcher2.group(2)
                                .replaceAll("[\\[\\](){}]","")
                                .replace(" ","")
                                .split(",");
                        String[] rows = matcher2.group(3)
                                .replaceAll("[\\[\\](){}]","")
                                .replace(" ","")
                                .replace("'","")
                                .replace("\"","")
                                .split(",");
                        ArrayList<String[]> rowValues = new ArrayList<>();
                        rowValues.add(rows);
                        Table table1 = new Table();
                        table1.setColNames(columns);
                        table1.setData(rowValues);
                        DBResponse DBResponse = db.TabInsert(table,table1);
                        if(!DBResponse.getStatus()){
                            System.out.println("ERROR: "+ DBResponse.getMessage());
                        }
                        else {
                            LogQueryHandler.addQueryLog("Success",queryString);
                        }
                    }
                    else {
                        LogQueryHandler.addQueryLog("Failed",queryString);
                        System.out.println("Not a valid Query");
                    }
                    break;

                case "update":
                   Pattern pat = Pattern.compile("update\\s(.*?)set\\s(.*?)where\\s(.*?)?;");
                    Matcher matches = pat.matcher(queryString);
                    boolean mat1 = matches.matches ();
                    if(mat1) {
                        String nameOfTab = matches.group (1).trim();
                        String[] colNAndV = matches.group(2).split("=");
                        String[] cond = matches.group (3).split(" ");
                        colNAndV[1] = colNAndV[1].substring(1, colNAndV[1].length()-2);

                        WhereCondition whereCond=new WhereCondition();
                        whereCond.setCol(cond[0]);
                        whereCond.setActions(typeOfActionGet(cond[1]));
                        whereCond.setValue(cond[2].replaceAll("\'",""));
                        colNAndV[1].replaceAll("\'","");

                        DBResponse DBResponse = db.TabUpdate(nameOfTab,colNAndV[0].trim(),colNAndV[1].trim(),whereCond);
                        if(!DBResponse.getStatus()){
                            System.out.println("ERROR Occur: "+ DBResponse.getMessage());
                        }
                        else {
                            LogQueryHandler.addQueryLog("Success",queryString);
                        }
                    }
                    else {
                        LogQueryHandler.addQueryLog("Failed",queryString);
                        System.out.println("Not a valid Query");
                    }
                    break;
                case "drop":
                    Pattern pat9 = Pattern.compile("drop\\s(.*?)table\\s(.*?)?;");
                    Matcher mat10 = pat9.matcher(queryString);
                    boolean isMatch = mat10.matches();
                    if (isMatch) {
                        String nameOfTable = mat10.group(2);
                        db.TabDrop(nameOfTable);
                        LogEventHandler.insertLogEvent("Table : "+ nameOfTable +" Dropped from Database "+ Constant.CURRENT_DB);
                        LogQueryHandler.addQueryLog("Success",queryString);
                    }
                    else {
                        LogQueryHandler.addQueryLog("Failed",queryString);
                        System.out.println("Not a valid Query");
                    }
                    break;
                case "delete":
                    Pattern pat6 = Pattern.compile("delete\\s(.*?)from\\s(.*?)where\\s(.*?)?;");
                    Matcher mat6 = pat6.matcher(queryString);
                    boolean mat12 = mat6.matches ();
                    if(mat12) {
                        String[] cond = mat6.group (3).split(" ");
                        String nameOfTable = mat6.group (2).trim();

                        WhereCondition whereC=new WhereCondition();
                        whereC.setValue(cond[2].replaceAll("\'",""));
                        whereC.setActions(typeOfActionGet(cond[1]));
                        whereC.setCol(cond[0]);

                        DBResponse DBResponse = db.TabDelete(nameOfTable,whereC);
                        if(!DBResponse.getStatus()){
                            System.out.println("ERROR: "+ DBResponse.getMessage());
                        }
                        else {
                            LogQueryHandler.addQueryLog("Success",queryString);
                        }
                    }
                    else {
                        LogQueryHandler.addQueryLog("Failed",queryString);
                    }
                    break;
            }


    }



}

