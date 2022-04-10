package Logging;

import CustomExceptions.DBException;
import Services.MetadataServices;
import Services.impl.MetadataServicesImpl;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class GeneralLogWriter {
    private static String GENERAL_LOG_FILE = "src/general_logs.json";

    public static void addGeneralLog(String message) {
        File file = new File(GENERAL_LOG_FILE);

        if (!file.exists()) {
            try {
                boolean isNewFileCreated = file.createNewFile();
                if (isNewFileCreated) {
                    addLog(message, file);
                }
            } catch (final IOException e) {
                e.printStackTrace();
            }
        } else {
            addLog(message, file);
        }
    }

    private static void addLog(String message, File file) {

        try (FileWriter writer = new FileWriter(file, true)) {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH:mm:ss");
            LocalDateTime now = LocalDateTime.now();
            String timestamp = dtf.format(now);
            String jsonRecord = "{\"Query Execution Time\":\"" + timestamp + "\",\"State Of DB\":" + message + "}";
            writer.append(jsonRecord);
            writer.append("\n");
        } catch (final IOException e) {
            e.printStackTrace();
        }

    }

    public static void addMetadata() {
        MetadataServices meta = new MetadataServicesImpl();
        List<String> databases;
        String message = "{";
        try {
            databases = meta.getDBs();
            for (String database : databases) {
                List<String> tables;
                tables = meta.getTablesInDBs(database);
                message = "{\"DatabaseName\":\"" + database + "\",\"NoOfTables\":\"" + tables.size() + "\",\"Tables\":\"" + String.join(",", tables) + "\"},";
            }
            if (databases.size()>0) {
                message = message.substring(0, message.length() - 1);
            }
//            message += "}";
            addGeneralLog(message);
        } catch (DBException e) {
            e.printStackTrace();
        }
    }
}
