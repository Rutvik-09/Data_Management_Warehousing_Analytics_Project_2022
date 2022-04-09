package erdgeneration;

import java.io.*;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Iterator;
import java.util.Scanner;

import org.apache.commons.io.FileUtils;

public class ERDGeneration {

    public void generate(String username) throws IOException, NoSuchAlgorithmException {

        String encryptedUsername = encrypt(username);
        File directoryPath = new File("src/main/Users/"+encryptedUsername);
        String contents[] = directoryPath.list();
        System.out.println("Select the database for which you want to generate the ERD:");
        for(int i=0; i<contents.length; i++) {
            if(!contents[i].equals("erd")){
                System.out.println(contents[i]);
            }
        }

        Scanner sc=new Scanner(System.in);
        String databaseName=sc.next();
        System.out.println(databaseName);

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        Instant instant = timestamp.toInstant();
        Timestamp tsFromInstant = Timestamp.from(instant);
        //System.out.println(tsFromInstant.getTime());
        Iterator it = FileUtils.iterateFiles(new File("src/main/Users/"+encryptedUsername+"/"+databaseName+"/metadata"), null, false);
        BufferedWriter bw=new BufferedWriter(new FileWriter("src/main/Users/"+encryptedUsername+"/erd/"+tsFromInstant.getTime(),true));
        while(it.hasNext()) {
            BufferedReader br = new BufferedReader(new FileReader(((File) it.next()).getAbsolutePath()));
            String line = "";
            Integer lineNumber=0;
            while((line=br.readLine())!=null){
                //System.out.println(line);
                if(lineNumber==0){
                    String[] tableName=line.split("@");
                    String writeLine="["+tableName[1]+"]\n";
                    bw.write(writeLine);
                }else{
                    String[] words=line.split(" ");
                    //System.out.println(words);
                    if(words[0].equals("CONSTRAINT")){
                        String key1=words[3].replaceAll("key\\(","").replaceAll("key\\(","");
                        String writeLine=key1+" (1) -----> (N) "+words[5]+"\n";
                        bw.write(writeLine);
                    }else{
                        bw.write(line+"\n");
                    }
                }
                lineNumber=lineNumber+1;
            }
            bw.write("\n");
        }
        bw.close();
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
