import java.io.*;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Login {

    public boolean login(String username, String password,String answer,Integer questionNumber) throws IOException, NoSuchAlgorithmException {

        File file=new File("C:\\Pavan\\Database\\Project\\src\\User_Profile");
        BufferedReader br=new BufferedReader(new FileReader(file));

        String line;
        boolean loginStatus=false;
        String savedUsername;
        String savedPassword;
        String savedAnswer;

        String encryptedUsername= encrypt(username);
        String encryptedPassword= encrypt(password);

        while((line=br.readLine())!=null){
            String[] credentials=line.split("\\|");

            if(credentials.length>0){
                savedUsername=credentials[0];
                savedPassword=credentials[1];
                savedAnswer=credentials[questionNumber+2];

                if(savedUsername.equals(encryptedUsername) && savedPassword.equals(encryptedPassword)
                && savedAnswer.equals(answer)){
                    loginStatus=true;
                    break;
                }
            }
        }

        return loginStatus;
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
