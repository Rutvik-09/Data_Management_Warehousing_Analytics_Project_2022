package LogManagement;

import Constants.QueryConstants;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;

public class EventLogWriter {

    private static String loggedInUser = QueryConstants.CURRENT_USER;
    private static String EVENT_LOG_FILE = "src/event_logs.json";

    public static void addEventLog(String message) {
        File file = new File(EVENT_LOG_FILE);

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
            String userName = loggedInUser;
            String jsonRecord = "{\"TimeStamp\":\"" + timestamp + "\",\"UserName\":\"" + userName + "\",\"EventMessage\":\"" + message +  "\"}";
            writer.append(jsonRecord);
            writer.append("\n");
        } catch (final IOException e) {
            e.printStackTrace();
        }

    }
}
