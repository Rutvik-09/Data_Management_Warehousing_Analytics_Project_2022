package UserRegistration;

import CustomExceptions.DuplicateUser;
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

public class Registration {

    public boolean register(String username,String password,String answer1,String answer2,String answer3) throws IOException,
            DuplicateUser, NoSuchAlgorithmException {

        File file=new File("./src/main/java/UserRegistration/User_Profile");
        BufferedReader br=new BufferedReader(new FileReader(file));

        String line;
        boolean uniqueUsername=true;
        String savedUsername;

        String encryptedUsername= encrypt(username);
        String encryptedPassword= encrypt(password);

        while((line=br.readLine())!=null){
            String[] credentials=line.split("\\|");

            if(credentials.length>0){
                savedUsername=credentials[0];
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

        br.close();

        return true;
    }

    public boolean createUser(String username,String password,String answer1,String answer2,String answer3)
            throws IOException, NoSuchAlgorithmException {
        BufferedWriter bw=new BufferedWriter(new FileWriter("./src/main/java/UserRegistration/User_Profile",true));

        String line=username+"|"+password+"|"+answer1+"|"+answer2+"|"+answer3+"\n";

        bw.write(line);
        bw.close();

        return true;
    }

    public boolean createDirectory(String username) {

        String path= "./src/main/java/UserRegistration/User_Profile"+username;

        File file=new File(path);

        boolean status=false;

        status=file.mkdir();

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
