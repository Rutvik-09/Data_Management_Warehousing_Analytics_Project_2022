import CustomExceptions.DuplicateUser;
import UserRegistration.Login;
import UserRegistration.Registration;

import javax.crypto.NoSuchPaddingException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;
import java.util.Random;

public class DatabaseDriver {
    public static void main(String[] args) throws NoSuchPaddingException, NoSuchAlgorithmException, UnsupportedEncodingException {
        String username;
        String password;
        String[] security_questions=new String[]
                {"Enter the current city that you live in.","Name of your best friend","Enter your mother's maiden name"};
        int[] question_numbers = {1, 2, 3};

        int question_number;
        String answer;

        Scanner sc=new Scanner(System.in);
        int choice;
        boolean status=false;

        while (true) {
            System.out.println("Enter your option: ");
            System.out.println("1. User User Registration");
            System.out.println("2. User User Login");
            System.out.println("3. Quit");
            choice = sc.nextInt();
            switch (choice) {
                case 1:
                    System.out.println("Please enter a username.");
                    username = sc.next();
                    System.out.println("Please enter a password.");
                    password = sc.next();
                    System.out.println("Enter the current city that you live in.");
                    String answer_1 = sc.next();
                    System.out.println("Name of your best friend");
                    String answer_2 = sc.next();
                    System.out.println("Enter your mother's maiden name");
                    String answer_3 = sc.next();
                    Registration rg = new Registration();
                    try {
                        status = rg.register(username, password, answer_1, answer_2, answer_3);
                    } catch (IOException | NoSuchAlgorithmException | DuplicateUser e) {
                        e.printStackTrace();
                    }

                    if (status) {
                        System.out.println("User Registration Successfull");
                    } else {
                        System.out.println("User Registration Unsuccesfull");
                    }
                    break;

                case 2:
                    System.out.println("Please enter a username.");
                    username = sc.next();
                    System.out.println("Please enter a password.");
                    password = sc.next();
                    question_number = new Random().nextInt(question_numbers.length);
                    System.out.println(security_questions[question_number]);
                    answer = sc.next();
                    Login login = new Login();
                    try {
                        status = login.login(username, password, answer, question_number);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    if (status == true) {
                        System.out.println("User Login Successfull");

                    } else {
                        System.out.println("User Login Unsuccesfull");
                    }

                    break;
            }
        }
    }
}
