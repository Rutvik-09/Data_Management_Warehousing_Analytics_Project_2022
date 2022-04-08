package QueryProcessing;

import Constants.RegexConstants;
import Util.UtilModel;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CreateTable {
    public static Pattern pattern;
    public static Matcher matcher;

    public static boolean checkCreateTableSyntax(String input) {
        pattern = Pattern.compile(RegexConstants.CREATE_TABLE);
        UserInputProcessing userInputProcessing = new UserInputProcessing();
        matcher = pattern.matcher(input);
        if (matcher.find()) {
            String words[] = userInputProcessing.processUserInput(input);
      /*      try {
           //     createTable(words[2]);
            } catch (IOException e) {
                e.printStackTrace();
            }*/

            System.out.println("Table created successfully");
            return true;
        } else {
            System.out.printf("\nInvalid syntax for create database. \n");
            return false;
        }
    }

   /* public static boolean createTable(String tableName) throws IOException {
        UtilModel utilModel = new UtilModel();
        if (utilModel.getDatabaseName() == null) {
            System.out.println("No database selected.\n");
        } else {
            File tablePath = new File("./src/main/database/users/" + tableName + ".txt");
            File metadataTablePath = new File("./src/main/database/metadata/" + tableName + ".txt");
            PrintWriter writer = new PrintWriter(tablePath);

            if (!tablePath.exists()) {
                tablePath.createNewFile();
                metadataTablePath.createNewFile();
                writer.write("File created");

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

            } else {
                System.out.println("Table already exist\n");
            }
            writer.close();
            return true;
        }
    }*/
}






