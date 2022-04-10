package Logging;

import Constants.QueryConstants;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CrashLogWriter {
    private static String CRASH_LOG_FILE = "src/crash_logs.json";

    public static void addCrashLog(String message) {
        File file = new File(CRASH_LOG_FILE);

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
            String userName = QueryConstants.CURRENT_USER;
            String jsonRecord = "{\"TimeStamp\":\"" + timestamp + "\",\"UserName\":\"" + userName + "\",\"Message\":\"" + message +  "\"}";
            writer.append(jsonRecord);
            writer.append("\n");
        } catch (final IOException e) {
            e.printStackTrace();
        }

    }
}
