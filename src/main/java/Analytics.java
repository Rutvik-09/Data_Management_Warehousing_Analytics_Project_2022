
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;

public class Analytics {

    public static void updateanalytics() throws IOException {
        FileWriter writer = new FileWriter("analysisfiles/updateanalytics.txt",true);
        Scanner reader = new Scanner(new File("dummydata.txt"));
        ArrayList<String> tablename = new ArrayList<>();
        ArrayList<Integer> tablenamecount = new ArrayList<>();
        while(reader.hasNext()){

            String[] splited = reader.nextLine().toLowerCase().split(",");
            if(splited[1].contains("update")){

            String[] updatequery = splited[1].split(":");
            String[] updatequerysplit = updatequery[1].split(" ");
                //System.out.println(updatequerysplit[1]);

                if(!tablename.contains(updatequerysplit[1])){
                    tablename.add(updatequerysplit[1]);
                    tablenamecount.add(1);

                }
               else if(tablename.contains(updatequerysplit[1])){
                for(int i=0; i<tablename.size(); i++)
                {
                  if(tablename.get(i).equals(updatequerysplit[1])){
                     tablenamecount.set(i, tablenamecount.get(i)+1);
                  }
                }
                }
            }
        }

        for (int i=0;i<tablename.size();i++){

            writer.write("Total "+tablenamecount.get(i)+" Update operations are performed on "+tablename.get(i));
            System.out.println("Total "+tablenamecount.get(i)+" Update operations are performed on "+tablename.get(i));
            writer.write("\n");
        }
        writer.close();
    }

    public static void queryanalytics(String username) throws IOException {
        FileWriter writer = new FileWriter("analysisfiles/queryanalytics.txt",true);
        Scanner reader = new Scanner(new File("user/"+username+"/log.txt"));
        ArrayList<String> dbname = new ArrayList<>();
        ArrayList<Integer> queryct = new ArrayList<>();

        while(reader.hasNext()){

           // System.out.println(reader.nextLine());
            String[] splited = reader.nextLine().toLowerCase().split(",");
            //System.out.println(splited[4]);
            String[] splitedb = splited[4].split(":");
            String[] db = splitedb[1].split(" ");
            //System.out.println(db[0]);
            if(!dbname.contains(db[0])){
                dbname.add(db[0]);
                queryct.add(1);
            }
            else if(dbname.contains(db[0])){
                for(int i=0;i< dbname.size();i++){
                    if(dbname.get(i).equals(db[0])){
                        queryct.set(i, queryct.get(i)+1);
                    }
                }
            }

        }
        for(int i=0;i< dbname.size();i++){
            writer.write("user "+username+" submitted "+queryct.get(i)+" queries for "+dbname.get(i)+" running on ");
            System.out.println("user "+username+" submitted "+queryct.get(i)+" queries for "+dbname.get(i)+" running on ");
            writer.write("\n");
        }
        writer.close();
    }


    public static void main(String[] args) throws IOException {
       updateanalytics();
       queryanalytics("rutvik");
    }
}
