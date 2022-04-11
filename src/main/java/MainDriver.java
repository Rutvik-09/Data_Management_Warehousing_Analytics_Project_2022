import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;
import customexceptions.DuplicateUser;
import databasecreds.DatabaseCreds;
import distributeddatabaseoperations.DistributedDatabaseOperations;
import loginregistration.Login;
import loginregistration.Register;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public class MainDriver {
    public static void main(String[] args) throws JSchException, SftpException {

        // VM1 Reading Testing

        //DatabaseCreds databaseCreds=new DatabaseCreds();
        //DistributedDatabaseOperations distributedDatabaseOperations=new DistributedDatabaseOperations(databaseCreds.getVM1_USERNAME(), databaseCreds.getVM1_PORT(), databaseCreds.getVM1_HOST());
        //distributedDatabaseOperations.getListOfDirectories("/home/dmw");


        // VM2 Reading Testing

        databasecreds.DatabaseCreds databaseCreds=new databasecreds.DatabaseCreds();
        DistributedDatabaseOperations distributedDatabaseOperations=new DistributedDatabaseOperations(databaseCreds.getVM2_USERNAME(), databaseCreds.getVM2_PORT(), databaseCreds.getVM2_HOST());
        //distributedDatabaseOperations.getListOfDirectories("/home/dmw");


        // VM1 Create Directory

        //databasecreds.DatabaseCreds databaseCreds=new databasecreds.DatabaseCreds();
        //DistributedDatabaseOperations distributedDatabaseOperations=new DistributedDatabaseOperations(databaseCreds.getVM1_USERNAME(), databaseCreds.getVM1_PORT(), databaseCreds.getVM1_HOST());
        /*
        distributedDatabaseOperations.createDirectory("/home/dmw/databases");
        distributedDatabaseOperations.createDirectory("/home/dmw/databases/university");
        distributedDatabaseOperations.createDirectory("/home/dmw/erd");
        distributedDatabaseOperations.createDirectory("/home/dmw/databases/university/metadata");
        distributedDatabaseOperations.createDirectory("/home/dmw/databases/university/tables");
        //List<String> list=distributedDatabaseOperations.getListOfDirectories("/home/dmw");

         */
        //for(String entry:list){
        //    System.out.println(entry);
        //}

        distributedDatabaseOperations.removeFile("/home/dmw/databases/university/metadata/classes.txt");
        distributedDatabaseOperations.removeFile("/home/dmw/databases/university/metadata/classes_1.txt");
        // VM2 Create Directory
        /*
        databasecreds.DatabaseCreds databaseCreds=new databasecreds.DatabaseCreds();
        DistributedDatabaseOperations distributedDatabaseOperations=new DistributedDatabaseOperations(databaseCreds.getVM2_USERNAME(), databaseCreds.getVM2_PORT(), databaseCreds.getVM2_HOST());
        distributedDatabaseOperations.createDirectory("/home/dmw/users");
        distributedDatabaseOperations.getListOfDirectories("/home/dmw");
        */

        // VM1 Write/Upload File

        //databasecreds.DatabaseCreds databaseCreds=new databasecreds.DatabaseCreds();
        //DistributedDatabaseOperations distributedDatabaseOperations=new DistributedDatabaseOperations(databaseCreds.getVM1_USERNAME(), databaseCreds.getVM1_PORT(), databaseCreds.getVM1_HOST());
        distributedDatabaseOperations.writeToFile("/home/dmw/databases/university/metadata/classes.txt","@classes\nclass_id INTEGER(11),\nclass_name VARCHAR(255),\nPRIMARY KEY(class_id)\n");
        distributedDatabaseOperations.writeToFile("/home/dmw/databases/university/metadata/classes_1.txt","@classes_1\nprimary_id INTEGER(11),\nclass_id INTEGER(11),\nclass_name VARCHAR(255),\nPRIMARY KEY(primary_id)\nCONSTRAINT fk_class_id FOREIGN KEY(class_id) REFERENCES classes(class_id)\n");
        //distributedDatabaseOperations.getListOfDirectories("/home/dmw/sampleDir007");


        // VM2 Write/Upload File
        /*
        databasecreds.DatabaseCreds databaseCreds=new databasecreds.DatabaseCreds();
        DistributedDatabaseOperations distributedDatabaseOperations=new DistributedDatabaseOperations(databaseCreds.getVM2_USERNAME(), databaseCreds.getVM2_PORT(), databaseCreds.getVM2_HOST());
        distributedDatabaseOperations.writeToFile("/home/dmw/sampleDir007/upload.txt","Sample file contents");
        distributedDatabaseOperations.getListOfDirectories("/home/dmw/sampleDir007");
        */

        // VM1 Read File
        //System.out.println("----------------------------------");
        //databasecreds.DatabaseCreds databaseCreds=new databasecreds.DatabaseCreds();
        //DistributedDatabaseOperations distributedDatabaseOperations=new DistributedDatabaseOperations(databaseCreds.getVM1_USERNAME(), databaseCreds.getVM1_PORT(), databaseCreds.getVM1_HOST());
        //String fileContents_1=distributedDatabaseOperations.readFile("/home/dmw/users");
        //System.out.println(fileContents_1);


        // VM2 Read File
        /*
        databasecreds.DatabaseCreds databaseCreds=new databasecreds.DatabaseCreds();
        DistributedDatabaseOperations distributedDatabaseOperations=new DistributedDatabaseOperations(databaseCreds.getVM2_USERNAME(), databaseCreds.getVM2_PORT(), databaseCreds.getVM2_HOST());
        String fileContents=distributedDatabaseOperations.readFile("/home/dmw/sampleDir007/upload.txt");
        System.out.println(fileContents);
         */

        // Remove File
        //distributedDatabaseOperations.removeFile("/home/dmw/sampleDir007/upload.txt");

        // Remove Directory
        //distributedDatabaseOperations.removeDirectory("/home/dmw/sampleDir007");

        //distributedDatabaseOperations.getListOfDirectories("/home/dmw");
        //System.out.println("----------------------------------");
        //distributedDatabaseOperations.appendToFile("/home/dmw/user_profiles.txt","13027a6893cd7c75d773ff318fdf99c13a37ade0|afe22608005190b8cc3701d3c8eb99f6a92662cd|halifax|meet|mother\n");

        //String fileContents=distributedDatabaseOperations.readFile("/home/dmw/sampleDir007/upload007.txt");
        //System.out.println(fileContents);
        /*
        Register registerObject=new Register();
        try {
            registerObject.register("ruthvik","ruthvik@123","ruthvik@halifax","navya","geetha");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (DuplicateUser e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
         */
        /*
        Login loginObject=new Login();
        try {
            boolean status=loginObject.login("ruthvik","ruthvik@123","navya",1);
            System.out.println(status);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        */
    }
}