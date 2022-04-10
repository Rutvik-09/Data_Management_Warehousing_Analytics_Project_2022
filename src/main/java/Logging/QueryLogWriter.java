package Logging;

import Constants.QueryConstants;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class QueryLogWriter {

    private static String loggedInUser = QueryConstants.CURRENT_USER;
    private static String QUERY_LOG_FILE = "src/query_logs.json";

    public static void addQueryLog(String query_status, String query_string) {
        File file = new File(QUERY_LOG_FILE);

        if (!file.exists()) {
            try {
                boolean isNewFileCreated = file.createNewFile();
                if (isNewFileCreated) {
                    addLog(query_status, query_string, file);
                }
            } catch (final IOException e) {
                e.printStackTrace();
            }
        } else {
            addLog(query_status, query_string, file);
        }
    }

    private static void addLog(String query_status, String query_string, File file) {

        try (FileWriter writer = new FileWriter(file, true)) {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH:mm:ss");
            LocalDateTime now = LocalDateTime.now();
            String timestamp = dtf.format(now);
            String userName = loggedInUser;
            String query = query_string;
            String status = query_status;
            String dbName = QueryConstants.CURRENT_DB;
            String vm = QueryConstants.CURRENT_VM;
            String jsonRecord = "{\"Status\":\"" + status + "\"|\"Query\":\"" + query + "\"|\"TimeStamp\":\"" + timestamp + "\"|\"UserName\":\"" + userName + "\"|\"DBName\":\"" + dbName + "\"|\"VM\":\"" + vm + "\"}";
            writer.append(jsonRecord);
            writer.append("\n");
        } catch (final IOException e) {
            e.printStackTrace();
        }

    }
}
