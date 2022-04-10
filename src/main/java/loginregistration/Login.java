package loginregistration;

import databasecreds.DatabaseCreds;
import distributeddatabaseoperations.DistributedDatabaseOperations;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Login {
    public boolean login(String username, String password,String answer,Integer questionNumber) throws IOException, NoSuchAlgorithmException, UnsupportedEncodingException {

        String line;
        boolean loginStatus=false;
        String savedUsername;
        String savedPassword;
        String savedAnswer;

        String encryptedUsername= encrypt(username);
        String encryptedPassword= encrypt(password);

        DatabaseCreds databaseCreds=new DatabaseCreds();

        DistributedDatabaseOperations distributedDatabaseOperations_1=new DistributedDatabaseOperations(databaseCreds.getVM1_USERNAME(), databaseCreds.getVM1_PORT(), databaseCreds.getVM1_HOST());
        String fileContents_1=distributedDatabaseOperations_1.readFile("/home/dmw/user_profiles.txt");

        String[] fileContentsList_1 = fileContents_1.split("\n");
        for(int i=0;i<fileContentsList_1.length;i++){
            String[] userCreds= fileContentsList_1[i].split("\\|");
            for(int j=0;j<userCreds.length;j++){
                //System.out.println(userCreds[j]);
                savedUsername=userCreds[0];
                savedPassword=userCreds[1];
                savedAnswer=userCreds[questionNumber+2];

                if(savedUsername.equals(encryptedUsername) && savedPassword.equals(encryptedPassword)
                        && savedAnswer.equals(answer)){
                    loginStatus=true;
                    break;
                }
            }
        }

        DistributedDatabaseOperations distributedDatabaseOperations_2=new DistributedDatabaseOperations(databaseCreds.getVM2_USERNAME(), databaseCreds.getVM2_PORT(), databaseCreds.getVM2_HOST());
        String fileContents_2=distributedDatabaseOperations_2.readFile("/home/dmw/user_profiles.txt");

        String[] fileContentsList_2 = fileContents_2.split("\n");
        for(int i=0;i<fileContentsList_2.length;i++){
            String[] userCreds= fileContentsList_2[i].split("\\|");
            for(int j=0;j<userCreds.length;j++){
                //System.out.println(userCreds[j]);
                savedUsername=userCreds[0];
                savedPassword=userCreds[1];
                savedAnswer=userCreds[questionNumber+2];

                if(savedUsername.equals(encryptedUsername) && savedPassword.equals(encryptedPassword)
                        && savedAnswer.equals(answer)){
                    loginStatus=true;
                    break;
                }
            }
        }

        return loginStatus;
    }

    public String encrypt(String field) throws UnsupportedEncodingException, NoSuchAlgorithmException, UnsupportedEncodingException {
        String encryptedMessage="";

        MessageDigest digest = MessageDigest.getInstance("SHA-1");
        digest.reset();
        digest.update(field.getBytes("utf8"));
        encryptedMessage = String.format("%040x", new BigInteger(1, digest.digest()));

        return encryptedMessage;
    }
}
