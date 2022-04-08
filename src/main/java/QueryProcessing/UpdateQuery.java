package QueryProcessing;

import Constants.RegexConstants;
import Util.UtilModel;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UpdateQuery {
        public static void update(String input) throws IOException {
        UtilModel utilModel = new UtilModel();
            if (utilModel.getDatabaseName()==null){
                System.out.println("Please select the database.");
            }
            else {
                Pattern pattern = Pattern.compile(RegexConstants.UPDATE_QUERY);
                Matcher matcher = pattern.matcher(input);
                if (matcher.find()){
                    String words[] = input.split("\\s+");
                    String tableName;
                    tableName = words[2];

                    List<String> tables = new ArrayList<>();
                    File directory = new File("./src/main/database/users/user1/" + utilModel.getDatabaseName());
                    File[] files = directory.listFiles();
                    if (files != null) {
                        for (File file : files) {
                            if (file.isDirectory()) {
                                tables.add(file.getName());
                            }
                        }
                    }
                    if (tables.contains(tableName)){
                        String oldValue;
                        String newValue;
                        String conditionColumn;
                        String columnToBeUpdated;
                        columnToBeUpdated = words[4];
                        conditionColumn = words[8];
                        oldValue = words[6];
                        newValue = words[10];
                        String line;
                        BufferedReader bufferedReader= new BufferedReader(new FileReader(new File("./src/main/database/users/user1/" + utilModel.getDatabaseName()+"/"+tableName+".txt")));
                        BufferedWriter bufferedWriter =  new BufferedWriter(new FileWriter(new File("./src/main/database/users/user1/" + utilModel.getDatabaseName()+"/"+tableName+".txt")));
                        while (bufferedReader.readLine()!=null){
                             line = bufferedReader.readLine();
                             if (line.contains(oldValue)){

                             }
                             else {
                                 System.out.println("value you want to update does not exist");
                             }
                        }

                    }
                    else {
                        System.out.println("Table you want to update does not exist.");
                    }

                }
                else {
                    System.out.println("Incorrect Syntax for Update Query");
                }

            }
        }

}

