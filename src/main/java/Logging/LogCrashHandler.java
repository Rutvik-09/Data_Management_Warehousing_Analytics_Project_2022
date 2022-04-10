package Logging;

import Constants.Constant;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LogCrashHandler {

    private static void insertLog(String message, File file) {

        try (FileWriter filewriter = new FileWriter(file, true)) {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH:mm:ss");
            LocalDateTime now = LocalDateTime.now();
            String timestamp = dtf.format(now);
            String userName = Constant.CURRENT_USER;
            String jsonRecord = "{\"TimeStamp\":\"" + timestamp + "\",\"UserName\":\"" + userName + "\",\"Message\":\"" + message +  "\"}";
            filewriter.append(jsonRecord);
            filewriter.append("\n");
        } catch (final IOException e) {
            e.printStackTrace();
        }

    }

    public static void insertLogCrash(String message) {
        File file = new File(CRASHLOGFILENAME);

        if (!file.exists()) {
            try {
                boolean fileCreated = file.createNewFile();
                if (fileCreated) {
                    insertLog(message, file);
                }
            } catch (final IOException e) {
                e.printStackTrace();
            }
        } else {
            insertLog(message, file);
        }
    }

    private static String CRASHLOGFILENAME = "src/crash_logs.json";

}
