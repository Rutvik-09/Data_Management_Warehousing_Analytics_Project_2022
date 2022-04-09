import java.io.IOException;
import customexceptions.DuplicateUser;
import loginregistration.Login;
import loginregistration.LoginSuccess;
import loginregistration.Register;
import javax.crypto.NoSuchPaddingException;
import java.io.FileNotFoundException;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;
import java.util.Random;

public class DatabaseDriver {
    public static void main(String[] args) throws NoSuchPaddingException, NoSuchAlgorithmException, IOException {
        String username;
        String password;
        String[] security_questions=new String[]
                {"Enter the current city that you live in.","Name of your best friend","Enter your mother's maiden name"};
        int[] question_numbers = {1, 2, 3};

        int question_number;
        String answer;

        Scanner sc=new Scanner(System.in);
        Integer choice=0;
        Boolean status=false;

        while(choice!=3){
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
                    Register rg=new Register();
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
                        LoginSuccess loginSuccess=new LoginSuccess();
                        loginSuccess.mainMenu(username);
                    }else{
                        System.out.println("Login Unsuccesfull");
                    }

                    break;
                case 3:

                    break;
            }
        }
    }
}
