package QueryProcessing;

import Constants.RegexConstants;
import Util.UtilModel;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UseDatabaseQuery {
    public static void useDatabase(String input){
        UtilModel utilModel=new UtilModel();

        Pattern pattern = Pattern.compile(RegexConstants.USE_DATABASE);
        Matcher matcher = pattern.matcher(input);
        if (matcher.find()){

                List<String> directories = new ArrayList<>();
                File directory = new File("./src/main/database/users/user1");
                File[] files = directory.listFiles();
                if (files!=null){
                    for (File file : files) {
                        if (file.isDirectory()) {
                            directories.add(file.getName());
                        }
                    }
               }
                String words[] = input.split("\\s+");
                utilModel.setDatabaseName(words[2]);

        }
        else
        {
            System.out.printf("\nInvalid syntax for use database. \n");
        }
        System.out.println("Database Selected:  " + utilModel.getDatabaseName());
    }

}
