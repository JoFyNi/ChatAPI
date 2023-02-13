package v1;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class message {

    String from;
    String content;
    static BufferedWriter writer;
    static String file = "messages.txt";

    public message(String from, String content) {
        this.from = from;
        this.content = content;
    }

    public static void save(String from, String content) {
        try {
            writer = new BufferedWriter(new BufferedWriter(new FileWriter(file))); // new StringBuilder().append(from).append(content)
            writer.write(from + ": " + content);
            writer.newLine();
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
