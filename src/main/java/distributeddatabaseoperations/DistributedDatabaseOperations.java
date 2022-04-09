package distributeddatabaseoperations;

import com.jcraft.jsch.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class DistributedDatabaseOperations {

    private String username;
    private Integer port;
    private String host;

    // Pass the details of VM to which you want to connect
    public DistributedDatabaseOperations(String username,Integer port,String host){
        this.username=username;
        this.port=port;
        this.host=host;
    }

    // Use this function to get a list of database/tables
    public List<String> getListOfDirectories(String path) throws JSchException, SftpException {

        List<String> list=new ArrayList<String>();

        JSch jsch=new JSch();
        jsch.addIdentity("pavan");

        Session session = jsch.getSession(this.username, this.host,this.port);
        session.setConfig("StrictHostKeyChecking", "no");
        session.connect();

        ChannelSftp channel = (ChannelSftp) session.openChannel("sftp");
        channel.connect();

        Vector<ChannelSftp.LsEntry> entries = channel.ls(path);

        //System.out.println("Entries in root directory:");
        for (ChannelSftp.LsEntry entry : entries) {
            //System.out.println(entry.getFilename());
            list.add(entry.getFilename());
        }

        channel.disconnect();

        session.disconnect();

        return list;
    }

    // Use this function to create database
    public boolean createDirectory(String path){
        JSch jsch=new JSch();

        try{
            jsch.addIdentity("pavan");

            Session session = jsch.getSession(this.username, this.host,this.port);
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();

            ChannelSftp channel = (ChannelSftp) session.openChannel("sftp");
            channel.connect();

            channel.mkdir(path);

            channel.disconnect();

            session.disconnect();
        } catch (JSchException e) {
            e.printStackTrace();
            return false;
        } catch (SftpException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    // Use this function to insert data to a new table for table which already has the data use appendToFile function
    public boolean writeToFile(String path, String fileContents) {
        JSch jsch=new JSch();
        try {
            jsch.addIdentity("pavan");
        } catch (JSchException e) {
            e.printStackTrace();
            return false;
        }

        Session session = null;
        try {
            session = jsch.getSession(this.username, this.host,this.port);
        } catch (JSchException e) {
            e.printStackTrace();
            return false;
        }
        session.setConfig("StrictHostKeyChecking", "no");
        try {
            session.connect();
        } catch (JSchException e) {
            e.printStackTrace();
            return false;
        }

        ChannelSftp channel = null;
        try {
            channel = (ChannelSftp) session.openChannel("sftp");
        } catch (JSchException e) {
            e.printStackTrace();
            return false;
        }

        try {
            channel.connect();
        } catch (JSchException e) {
            e.printStackTrace();
            return false;
        }

        String contents = fileContents;
        try {
            channel.put(new ByteArrayInputStream(contents.getBytes(StandardCharsets.UTF_8)), path);
        } catch (SftpException e) {
            e.printStackTrace();
            return false;
        }

        channel.disconnect();

        session.disconnect();

        return true;
    }

    // Use this function to select table
    public String readFile(String path){
        JSch jsch=new JSch();

        String fileContents=null;

        try{
            jsch.addIdentity("pavan");

            Session session = jsch.getSession(this.username, this.host,this.port);
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();

            ChannelSftp channel = (ChannelSftp) session.openChannel("sftp");
            channel.connect();

            InputStream inputStream = channel.get(path);
            byte[] bytes = inputStream.readAllBytes();
            fileContents=new String(bytes);
            System.out.println("Contents of file upload.txt: " + fileContents);

            channel.disconnect();

            session.disconnect();
        } catch (JSchException e) {
            e.printStackTrace();
            return null;
        } catch (SftpException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return fileContents;
    }

    // Use this function to delete table
    public boolean removeFile(String path){
        JSch jsch=new JSch();

        try{
            jsch.addIdentity("pavan");

            Session session = jsch.getSession(this.username, this.host,this.port);
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();

            ChannelSftp channel = (ChannelSftp) session.openChannel("sftp");
            channel.connect();

            channel.rm(path);

            channel.disconnect();

            session.disconnect();
        } catch (JSchException e) {
            e.printStackTrace();
            return false;
        } catch (SftpException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    // Use this function to delete database
    public boolean removeDirectory(String path){
        JSch jsch=new JSch();

        try{
            jsch.addIdentity("pavan");

            Session session = jsch.getSession(this.username, this.host,this.port);
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();

            ChannelSftp channel = (ChannelSftp) session.openChannel("sftp");
            channel.connect();

            // Delete all the file first
            List<String> list= getListOfDirectories(path);
            for (String entry : list) {
                if(!entry.equals(".") && !entry.equals("..")){
                    //System.out.println(entry.getFilename());
                    boolean status=removeFile(path+"/"+entry);
                }

            }

            channel.rmdir(path);

            channel.disconnect();

            session.disconnect();
        } catch (JSchException e) {
            e.printStackTrace();
            return false;
        } catch (SftpException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    // Use this to append to exsisting file (Insert data to tabl which already has some table)
    public boolean appendToFile(String path, String fileContents) {
        JSch jsch=new JSch();
        try {
            jsch.addIdentity("pavan");
        } catch (JSchException e) {
            e.printStackTrace();
            return false;
        }

        Session session = null;
        try {
            session = jsch.getSession(this.username, this.host,this.port);
        } catch (JSchException e) {
            e.printStackTrace();
            return false;
        }
        session.setConfig("StrictHostKeyChecking", "no");
        try {
            session.connect();
        } catch (JSchException e) {
            e.printStackTrace();
            return false;
        }

        ChannelSftp channel = null;
        try {
            channel = (ChannelSftp) session.openChannel("sftp");
        } catch (JSchException e) {
            e.printStackTrace();
            return false;
        }

        try {
            channel.connect();
        } catch (JSchException e) {
            e.printStackTrace();
            return false;
        }

        String contents = fileContents;
        try {
            channel.put(new ByteArrayInputStream(contents.getBytes(StandardCharsets.UTF_8)), path,ChannelSftp.APPEND);
        } catch (SftpException e) {
            e.printStackTrace();
            return false;
        }

        channel.disconnect();

        session.disconnect();

        return true;
    }
}
