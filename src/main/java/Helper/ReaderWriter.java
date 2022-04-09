package Helper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ReaderWriter {

    private ReaderWriter() { }

    public static void print(String message) {
        System.out.println(message);
    }

    public static String input() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String input = reader.readLine();
        return input;
    }

}
