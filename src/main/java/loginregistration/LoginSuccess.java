package loginregistration;

import erdgeneration.ERDGeneration;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

public class LoginSuccess {
    public void mainMenu(String username) throws IOException, NoSuchAlgorithmException {
        String encryptedUsername= encrypt(username);
        Integer choice=0;
        Scanner sc=new Scanner(System.in);
        while(choice!=5){
            System.out.println("Enter your option: ");
            System.out.println("1. Execute SQL Query");
            System.out.println("2. ERD Generation");
            System.out.println("3. Export SQL Dump");
            System.out.println("4. Analytics");
            System.out.println("5. Logout");
            choice=sc.nextInt();
            switch (choice) {
                case 1:

                    break;
                case 2:
                    ERDGeneration erdGeneration=new ERDGeneration();
                    erdGeneration.generate(username);
                    break;
                case 5:

                    break;
            }
        }
    }

    public String encrypt(String field) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        String encryptedMessage="";

        MessageDigest digest = MessageDigest.getInstance("SHA-1");
        digest.reset();
        digest.update(field.getBytes("utf8"));
        encryptedMessage = String.format("%040x", new BigInteger(1, digest.digest()));

        return encryptedMessage;
    }
}
