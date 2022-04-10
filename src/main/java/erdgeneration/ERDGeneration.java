package erdgeneration;

import java.io.*;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.*;

import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;
import databasecreds.DatabaseCreds;
import distributeddatabaseoperations.DistributedDatabaseOperations;
import org.apache.commons.io.FileUtils;

public class ERDGeneration {

    public void generate(String username) throws IOException, NoSuchAlgorithmException, JSchException, SftpException {

        //String encryptedUsername = encrypt(username);
        //File directoryPath = new File("src/main/Users);

        DatabaseCreds databaseCreds=new DatabaseCreds();

        DistributedDatabaseOperations distributedDatabaseOperations_1=new DistributedDatabaseOperations(databaseCreds.getVM1_USERNAME(), databaseCreds.getVM1_PORT(), databaseCreds.getVM1_HOST());
        List<String> directories_1 = distributedDatabaseOperations_1.getListOfDirectories("/home/dmw/databases");

        DistributedDatabaseOperations distributedDatabaseOperations_2=new DistributedDatabaseOperations(databaseCreds.getVM2_USERNAME(), databaseCreds.getVM2_PORT(), databaseCreds.getVM2_HOST());
        List<String> directories_2 = distributedDatabaseOperations_2.getListOfDirectories("/home/dmw/databases");

        List<String> combinedList= new ArrayList<String>();
        combinedList.addAll(directories_1);
        combinedList.addAll(directories_2);

        Set<String> hSet = new HashSet<String>();
        for (String x : combinedList){
            if(!x.equals("erd") && !x.equals(".") && !x.equals("..")){
                hSet.add(x);
            }
        }


        System.out.println("Select the database for which you want to generate the ERD:");

        for (String x : hSet)
            System.out.println(x);

        Scanner sc=new Scanner(System.in);
        String databaseName=sc.next();
        //System.out.println(databaseName);

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        Instant instant = timestamp.toInstant();
        Timestamp tsFromInstant = Timestamp.from(instant);

        String filePath="/home/dmw/databases/"+databaseName+"/metadata";

        String writeLine="";

        List<String> files_1 = distributedDatabaseOperations_1.getListOfDirectories(filePath);

        //System.out.println("Size of files: "+files_1.size());
        //System.out.println("File content: "+files_1.toString());

        files_1.remove(".");
        files_1.remove("..");

        for(int i=0;i<files_1.size();i++){
            String fileString = distributedDatabaseOperations_1.readFile(filePath+"/"+files_1.get(i));

            if(fileString!=null){
                String[] fileContents=fileString.split("\n");

                for(int j=0;j<fileContents.length;j++){
                    //System.out.println(fileContents[j]);
                    if(j==0){
                        String[] tableName=fileContents[j].split("@");
                        writeLine=writeLine+"["+tableName[1]+"]\n";
                    }else{
                        String[] words=fileContents[j].split(" ");
                        if(words[0].equals("CONSTRAINT")){
                            //System.out.println("In line 72");
                            String key1=words[3].replaceAll("key\\(","").replaceAll("key\\(","");
                            writeLine=writeLine+ key1+" (1) -----> (N) "+words[5]+"\n";
                        }else{
                            //System.out.println("In line 76");
                            writeLine=writeLine+fileContents[j]+"\n";
                            //System.out.println(writeLine);
                        }
                    }
                }
            }
            writeLine=writeLine+"\n";
        }

        List<String> files_2 = distributedDatabaseOperations_2.getListOfDirectories(filePath);

        System.out.println("Size of files: "+files_2.size());
        System.out.println("File content: "+files_2.toString());

        files_2.remove(".");
        files_2.remove("..");

        for(int i=0;i<files_2.size();i++){
            String fileString = distributedDatabaseOperations_2.readFile(filePath+"/"+files_2.get(i));

            if(fileString!=null){
                String[] fileContents=fileString.split("\n");

                for(int j=0;j<fileContents.length;j++){
                    //System.out.println(fileContents[j]);
                    if(j==0){
                        String[] tableName=fileContents[j].split("@");
                        writeLine=writeLine+"["+tableName[1]+"]\n";
                    }else{
                        String[] words=fileContents[j].split(" ");
                        if(words[0].equals("CONSTRAINT")){
                            //System.out.println("In line 72");
                            String key1=words[3].replaceAll("key\\(","").replaceAll("key\\(","");
                            writeLine=writeLine+ key1+" (1) -----> (N) "+words[5]+"\n";
                        }else{
                            //System.out.println("In line 76");
                            writeLine=writeLine+fileContents[j]+"\n";
                            //System.out.println(writeLine);
                        }
                    }
                }
            }
            writeLine=writeLine+"\n";
        }

        String writePath="/home/dmw/erd/"+tsFromInstant.getTime()+".txt";
        distributedDatabaseOperations_1.writeToFile(writePath,writeLine);
        distributedDatabaseOperations_2.writeToFile(writePath,writeLine);

        String readFromFile_1=distributedDatabaseOperations_1.readFile(writePath);
        System.out.println(readFromFile_1);

        String readFromFile_2=distributedDatabaseOperations_2.readFile(writePath);
        System.out.println(readFromFile_2);

    }

}
