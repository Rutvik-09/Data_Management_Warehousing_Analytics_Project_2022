import CustomExceptions.ExceptionDB;
import CustomExceptions.DuplicateUser;
import Logging.LogGeneralHandler;
import Services.impl.QueryImplementation;
import Helper.ReaderWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static Constants.Constant.*;

public class DatabaseDriver {
    public static void main(String[] args) throws NoSuchAlgorithmException, IOException {
        initialSetup();
        CURRENT_VM = "VM1"; // need to update accordingly
        Runnable generalLoggingRunnable = LogGeneralHandler::addMetadata;
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(generalLoggingRunnable, 0, 60, TimeUnit.SECONDS);
        String username;
        String password;
        String[] security_questions=new String[]
                {"Enter the current city that you live in.","Name of your best friend","Enter your mother's maiden name"};
        int[] question_numbers = {1, 2, 3};

        int question_number;
        String answer;

        Scanner sc=new Scanner(System.in);
        Integer choice;
        Boolean status=false;

        lp: while(true){
            System.out.println("Enter your option: ");
            System.out.println("1. User Registration");
            System.out.println("2. User Login");
            System.out.println("3. Quit");
            choice=sc.nextInt();
            switch (choice) {
                case 1:
                    System.out.println("Please enter a username.");
                    username=sc.next();
                    System.out.println("Please enter a password.");
                    password=sc.next();
                    System.out.println("Enter the current city that you live in.");
                    String answer_1=sc.next();
                    System.out.println("Name of your best friend");
                    String answer_2=sc.next();
                    System.out.println("Enter your mother's maiden name");
                    String answer_3=sc.next();
                    Registration rg=new Registration();
                    try {
                        status = rg.register(username,password,answer_1,answer_2,answer_3);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (DuplicateUser e) {
                        e.printStackTrace();
                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    }

                    if(status==true){
                        System.out.println("Registration Successfull");
                    }else{
                        System.out.println("Registration Unsuccesfull");
                    }
                    break;

                case 2:
                    System.out.println("Please enter a username.");
                    username=sc.next();
                    System.out.println("Please enter a password.");
                    password=sc.next();
                    question_number = new Random().nextInt(question_numbers.length);
                    System.out.println(security_questions[question_number]);
                    answer=sc.next();
                    Login login=new Login();
                    try {
                        status=login.login(username,password,answer,question_number);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    if(status==true){
                        System.out.println("Login Successfull");
                        menu();
                    }else{
                        System.out.println("Login Unsuccesfull");
                    }

                    break;
            }
        }
    }

    public static void menu() throws IOException, ExceptionDB {
        int userInput = 0;
        do {
            ReaderWriter.printMesages("MENU");
            ReaderWriter.printMesages("1.Write Queries");
            ReaderWriter.printMesages("2.Export Dump");
            ReaderWriter.printMesages("3.Data Modeling");
            ReaderWriter.printMesages("4.Analytics");
            ReaderWriter.printMesages("5.Exit");
            try {
                userInput = Integer.parseInt(ReaderWriter.takeInput());
                switch (userInput) {
                    case 1:
                        ReaderWriter.printMesages("Enter Query");
                        new QueryImplementation().readData();
                        break;
                    case 2:

                    case 3:
                    case 4:
                    case 5:
                        break;
                    default:
                        System.out.println("Not a Valid Option");
                        break;
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }while (userInput!=2);
        System.out.println("Exit");
    }

    public static boolean initialSetup() throws IOException {
        File mainDir = new File(DB_LOCATION_STABLE);
        if (!mainDir.isDirectory()){
            mainDir.mkdir();
        }
        //Create Meta folder if not exist
        File metaDir = new File(DB_LOCATION_STABLE + META_DATA_DIRECTORY);
        if (!metaDir.isDirectory()) {
            metaDir.mkdir();
        }

        //Create Columns details if not exist
        File columnDetailsTable = new File(metaDir.getAbsolutePath() + SLASH + COLMETA);
        if (!columnDetailsTable.isFile()) {
            columnDetailsTable.createNewFile();
            FileWriter fileWriter = new FileWriter(columnDetailsTable);
            fileWriter.write("TableName" + DELIMITER + "ColumnName" + DELIMITER + "ValueTypes" + DELIMITER + "Constraints" + ENDOFLINE);
            fileWriter.close();
        }

        //Create Table details if not exist
        File tableDetailsTable = new File(metaDir.getAbsolutePath() + SLASH + TABMETA);
        if (!tableDetailsTable.isFile()) {
            tableDetailsTable.createNewFile();
            FileWriter fileWriter = new FileWriter(tableDetailsTable);
            fileWriter.write("Database" + DELIMITER + "TableName" + ENDOFLINE);
            fileWriter.close();
        }

        return true;
    }

}
