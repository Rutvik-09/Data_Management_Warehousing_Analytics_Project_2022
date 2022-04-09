package loginregistration;

import customexceptions.DuplicateUser;
import databasecreds.DatabaseCreds;
import distributeddatabaseoperations.DistributedDatabaseOperations;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;

import java.io.*;
import java.security.NoSuchAlgorithmException;

public class Register {

    public boolean register(String username,String password,String answer1,String answer2,String answer3) throws IOException,
            DuplicateUser, NoSuchAlgorithmException {

        String line;
        boolean uniqueUsername=true;
        String savedUsername;

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
                if(savedUsername.equals(encryptedUsername)){
                    uniqueUsername=false;
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
                if(savedUsername.equals(encryptedUsername)){
                    uniqueUsername=false;
                    break;
                }
            }
        }

        if(!uniqueUsername){
            throw new DuplicateUser("Duplicate user with username: " + username );
        }else{
            boolean userCreationStatus=createUser(encryptedUsername,encryptedPassword,answer1,answer2,answer3);

            boolean directoryCreationStatus=createDirectory(encryptedUsername);
        }

        return true;
    }

    public boolean createUser(String username,String password,String answer1,String answer2,String answer3)
            throws IOException, NoSuchAlgorithmException {

        String line=username+"|"+password+"|"+answer1+"|"+answer2+"|"+answer3+"\n";

        DatabaseCreds databaseCreds=new DatabaseCreds();

        DistributedDatabaseOperations distributedDatabaseOperations_1=new DistributedDatabaseOperations(databaseCreds.getVM1_USERNAME(), databaseCreds.getVM1_PORT(), databaseCreds.getVM1_HOST());
        distributedDatabaseOperations_1.appendToFile("/home/dmw/user_profiles.txt",line);

        DistributedDatabaseOperations distributedDatabaseOperations_2=new DistributedDatabaseOperations(databaseCreds.getVM2_USERNAME(), databaseCreds.getVM2_PORT(), databaseCreds.getVM2_HOST());
        distributedDatabaseOperations_2.appendToFile("/home/dmw/user_profiles.txt",line);

        return true;
    }

    public boolean createDirectory(String username) {

        String path= "/home/dmw/users/"+username;

        boolean status=false;

        DatabaseCreds databaseCreds=new DatabaseCreds();

        DistributedDatabaseOperations distributedDatabaseOperations_1=new DistributedDatabaseOperations(databaseCreds.getVM1_USERNAME(), databaseCreds.getVM1_PORT(), databaseCreds.getVM1_HOST());
        status=distributedDatabaseOperations_1.createDirectory(path);

        DistributedDatabaseOperations distributedDatabaseOperations_2=new DistributedDatabaseOperations(databaseCreds.getVM2_USERNAME(), databaseCreds.getVM2_PORT(), databaseCreds.getVM2_HOST());
        status=distributedDatabaseOperations_2.createDirectory(path);

        return status;
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
