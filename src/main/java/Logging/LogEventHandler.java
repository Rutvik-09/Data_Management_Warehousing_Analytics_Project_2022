package Logging;

import Constants.Constant;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LogEventHandler {

    private static void insertLog(String message, File file) {

        try (FileWriter fileWriter = new FileWriter(file, true)) {
            //String timestamp = new Timestamp(System.currentTimeMillis()).toString();
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH:mm:ss");
            LocalDateTime now = LocalDateTime.now();
            String timestamp = dtf.format(now);
            String userName = Constant.CURRENT_USER;
            String jsonRecord = "{\"TimeStamp\":\"" + timestamp + "\",\"UserName\":\"" + userName + "\",\"EventMessage\":\"" + message +  "\"}";
            fileWriter.append(jsonRecord);
            fileWriter.append("\n");
        } catch (final IOException e) {
            e.printStackTrace();
        }

    }

    public static void insertLogEvent(String message) {
        File file = new File(EVENTLOGFILE);

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

    private static String loggedInUser = Constant.CURRENT_USER;
    private static String EVENTLOGFILE = "src/event_logs.json";



}
