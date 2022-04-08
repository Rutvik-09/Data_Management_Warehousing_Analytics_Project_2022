package QueryProcessing;

import Constants.RegexConstants;

import java.io.File;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CreateDatabase {
   public static Pattern pattern;
   public static Matcher matcher;
    public static boolean checkCreateDatabaseSyntax(String input){
        pattern = Pattern.compile(RegexConstants.CREATE_DATABASE);
        UserInputProcessing userInputProcessing =new UserInputProcessing();
        matcher = pattern.matcher(input);
        if (matcher.find()){
            createDatabase(userInputProcessing.processUserInput(input)[2].replaceAll(";",""));

            System.out.println("Database created successfully");
            return true;
        }
        else {
            System.out.printf("\nInvalid syntax for create database. \n");
            return false;
        }
    }
    public static boolean createDatabase(String databaseName){
        File databaseDirectory = new File("./src/main/database/users/user1/"+databaseName);
        if (!databaseDirectory.exists()){
            databaseDirectory.mkdirs();
        }
        else {
            System.out.printf("Database already exist.");
        }
        return true;
    }
}
