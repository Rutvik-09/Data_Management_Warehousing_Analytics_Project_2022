package LogManagement;

import CustomExceptions.DatabaseException;
import Services.MetadataServices;
import Services.impl.MetadataServicesImpl;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
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
            String timestamp = new Timestamp(System.currentTimeMillis()).toString();
            String jsonRecord = "{\"Query Execution Time\":\"" + timestamp + "\",\"State Of DB\":" + message + "}";
            writer.append(jsonRecord);
            writer.append("\n");
        } catch (final IOException e) {
            e.printStackTrace();
        }

    }

    public static void addMetadata() {
        MetadataServices meta = new MetadataServicesImpl();
        List<String> databases = new ArrayList<String>();
        String message = "{";
        try {
            databases = meta.getDatabases();
            for (String database : databases) {
                List<String> tables = new ArrayList<String>();
                tables = meta.getTables(database);
                message = "{\"DatabaseName\":\"" + database + "\",\"NoOfTables\":\"" + tables.size() + "\",\"Tables\":\"" + String.join(",", tables) + "\"},";
            }
            if (databases.size()>0) {
                message = message.substring(0, message.length() - 1);
            }
            message += "}";
            addGeneralLog(message);
        } catch (DatabaseException e) {
            e.printStackTrace();
        }
    }
}
