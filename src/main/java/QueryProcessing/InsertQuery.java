package QueryProcessing;

import Constants.RegexConstants;
import Util.UtilModel;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InsertQuery {
   public static void insert(String input) throws FileNotFoundException {

      UtilModel utilModel= new UtilModel();
      if (utilModel.getDatabaseName()!=null){
         System.out.println("Select the database first.");
      }
      else {
         Pattern pattern = Pattern.compile(RegexConstants.INSERT_QUERY);
         Matcher matcher = pattern.matcher(input);
         if (matcher.find()){
            String[] token = input.split("\\s+");

            String tableName = token[2].replaceAll("\\(","");
            BufferedReader bufferedReader =  new BufferedReader(new FileReader(new File("./src/main/database/users/"+utilModel.getDatabaseName()+"/"+tableName+".txt")));

         }
         else {
            System.out.println("Invalid syntax");
         }
      }

   }
}
