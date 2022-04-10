package Helper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ReaderWriter {

    public static String takeInput() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        return reader.readLine();
    }

    public static void printMesages(String message) {
        System.out.println(message);
    }

    private ReaderWriter() { }





}
