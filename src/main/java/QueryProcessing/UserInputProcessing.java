package QueryProcessing;

import Constants.RegexConstants;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserInputProcessing {
    public static void main(String[] args) {
        UserInputProcessing userInputProcessing= new UserInputProcessing();
        String input = userInputProcessing.fetchUserInput();
        userInputProcessing.checkQueryOrTransaction(input);

    }
    public String fetchUserInput(){
        Scanner sc = new Scanner(System.in);
        String input = sc.nextLine();
        return input;
    }
    public String[] processUserInput(String input){
        String words[]=input.split("\\s+");
        return words;
    }

    public void checkQueryOrTransaction(String input){
        Pattern pattern = Pattern.compile(RegexConstants.START_TRANSACTION);
        Matcher matcher = pattern.matcher(input);
        if (matcher.find()){
        }
        else {
            String words[] = processUserInput(input);
            if (words[0].trim().equals("create")){
                if (words[1].trim().equals("database")){
                    CreateDatabase.checkCreateDatabaseSyntax(input);
                }
                else if (words[1].trim().equals("table")){
                    CreateTable.checkCreateTableSyntax(input);
                }
                else {
                    System.out.println("Invalid Syntax! can't recognize query.\n");
                }
            }
            else if (words[0].trim().equals("insert")){

            }
            else if(words[0].trim().equals("update")){

            }
            else if ((words[0]).trim().equals("delete")){

            }
            else if ((words[0]).trim().equals("use")){
                UseDatabaseQuery.useDatabase(input);
            }
            else {
                System.out.printf("Invalid Syntax! cannot recognize query.");
            }
            }
        }
    }

