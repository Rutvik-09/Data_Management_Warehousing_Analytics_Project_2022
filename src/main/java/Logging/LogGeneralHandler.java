package Logging;

import CustomExceptions.ExceptionDB;
import Services.MetaServices;
import Services.impl.MetaImplementation;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class LogGeneralHandler {
    public static void addMetadata() {
        MetaServices meta = new MetaImplementation();
        List<String> databases;
        String message = "{";
        try {
            databases = meta.DBsGet();
            for (String database : databases) {
                List<String> tables;
                tables = meta.TablesInDBsGet(database);
                message = "{\"DatabaseName\":\"" + database + "\",\"NoOfTables\":\"" + tables.size() + "\",\"Tables\":\"" + String.join(",", tables) + "\"},";
            }
            if (databases.size()>0) {
                message = message.substring(0, message.length() - 1);
            }
//            message += "}";
            insertLogGeneral(message);
        } catch (ExceptionDB e) {
            e.printStackTrace();
        }
    }


    private static void insertLog(String message, File file) {

        try (FileWriter fileWriter = new FileWriter(file, true)) {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH:mm:ss");
            LocalDateTime now = LocalDateTime.now();
            String timestamp = dtf.format(now);
            String jsonRecord = "{\"Query Execution Time\":\"" + timestamp + "\",\"State Of DB\":" + message + "}";
            fileWriter.append(jsonRecord);
            fileWriter.append("\n");
        } catch (final IOException e) {
            e.printStackTrace();
        }

    }

    public static void insertLogGeneral(String message) {
        File file = new File(GENERALLOGFILENAME);

        if (!file.exists()) {
            try {
                boolean isNewFileCreated = file.createNewFile();
                if (isNewFileCreated) {
                    insertLog(message, file);
                }
            } catch (final IOException e) {
                e.printStackTrace();
            }
        } else {
            insertLog(message, file);
        }
    }

    private static String GENERALLOGFILENAME = "src/general_logs.json";




}
