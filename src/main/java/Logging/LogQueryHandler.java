package Logging;

import Constants.Constant;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LogQueryHandler {

    private static void insertLog(String query_status, String query_string, File file) {

        try (FileWriter fileWriter = new FileWriter(file, true)) {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH:mm:ss");
            LocalDateTime now = LocalDateTime.now();
            String timestamp = dtf.format(now);
            String userName = loggedInUser;
            String query = query_string;
            String status = query_status;
            String dbName = Constant.CURRENT_DB;
            String vm = Constant.CURRENT_VM;
            String jsonRecord = "{\"Status\":\"" + status + "\"|\"Query\":\"" + query + "\"|\"TimeStamp\":\"" + timestamp + "\"|\"UserName\":\"" + userName + "\"|\"DBName\":\"" + dbName + "\"|\"VM\":\"" + vm + "\"}";
            fileWriter.append(jsonRecord);
            fileWriter.append("\n");
        } catch (final IOException e) {
            e.printStackTrace();
        }

    }

    public static void addQueryLog(String query_status, String query_string) {
        File file = new File(QUERYLOGFILENAME);

        if (!file.exists()) {
            try {
                boolean fileCreatedCHeck = file.createNewFile();
                if (fileCreatedCHeck) {
                    insertLog(query_status, query_string, file);
                }
            } catch (final IOException e) {
                e.printStackTrace();
            }
        } else {
            insertLog(query_status, query_string, file);
        }
    }

    private static String loggedInUser = Constant.CURRENT_USER;
    private static String QUERYLOGFILENAME = "src/query_logs.json";




}
