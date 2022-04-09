package ExportStructure;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class SQL_Dump {

    public static void createquery(String username) throws IOException {

        File f1 = new File("E:/Dalhousie University Studies/Sem-2/Project/src/main/Users/" + username);
        String subfiles[] = f1.list();

            for(int j=0;j<subfiles.length;j++){

                if(!subfiles[j].equals("erd"))
                {
                   // System.out.println(subfiles[j]);
                    File f2 = new File("E:/Dalhousie University Studies/Sem-2/Project/src/main/Users/" + username + "/" + subfiles[j]);
                    String databases[] = f2.list();

                    File filelist = new File("E:/Dalhousie University Studies/Sem-2/Project/src/main/Users/" + username + "/" + subfiles[j] + "/" + databases[0]);
                    String [] flist = filelist.list();
                    for (int l=0;l<flist.length;l++){
                        FileWriter writer = new FileWriter("Export_Structure/exportdump.sql",true);
                        Scanner please =new Scanner(new File("E:/Dalhousie University Studies/Sem-2/Project/src/main/Users/" + username + "/" + subfiles[j] + "/" + databases[0]+"/"+flist[l]));
                        int count =0;
                        while (please.hasNext()){
                            String line = please.nextLine().replace("@","");
                                if(count ==0){
                                    writer.write("CREATE TABLE "+line+ "(");
                                    count++;
                                }
                                else if (count>0){
                                   count++;
                                   writer.write(line);
                               }
                           }
                           writer.write(");");
                           writer.write("\n");
                           writer.close();
                        }
                }
            }
        }

        public static void insertquery(String username) throws IOException {

            File f1 = new File("E:/Dalhousie University Studies/Sem-2/Project/src/main/Users/" + username);
            String subfiles[] = f1.list();

            for(int j=0;j<subfiles.length;j++){

                if(!subfiles[j].equals("erd"))
                {
                    // System.out.println(subfiles[j]);
                    File f2 = new File("E:/Dalhousie University Studies/Sem-2/Project/src/main/Users/" + username + "/" + subfiles[j]);
                    String databases[] = f2.list();

                    File filelist = new File("E:/Dalhousie University Studies/Sem-2/Project/src/main/Users/" + username + "/" + subfiles[j] + "/" + databases[1]);
                    String [] flist = filelist.list();
                    for (int l=0;l<flist.length;l++){
                        FileWriter writer = new FileWriter("Export_Structure/exportdump.sql",true);
                        Scanner please =new Scanner(new File("E:/Dalhousie University Studies/Sem-2/Project/src/main/Users/" + username + "/" + subfiles[j] + "/" + databases[1]+"/"+flist[l]));
                        writer.write("INSERT INTO "+flist[l].replace(".txt","")+ " VALUES (");

                        while (please.hasNext()){
                            String line = please.nextLine().replace("|",",");

                                writer.write(line);

                        }
                        writer.write(");");
                        writer.write("\n");
                        writer.close();
                    }
                }
            }

    }

    public static void main(String[] args) throws IOException {
        String username = "79c21071b25cec954b761d67849b5a84866bbecc";
        createquery(username);
        insertquery(username);
    }
}
