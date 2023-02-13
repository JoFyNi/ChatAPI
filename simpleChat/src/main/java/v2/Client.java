package v2;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("localhost", 8888);
        System.out.println("Connected to server");
        DataInputStream inputStream = new DataInputStream(socket.getInputStream());
        DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
        Scanner scanner = new Scanner(System.in);
        String message = "";
        while (!message.equals("exit chat")) {
            System.out.print("You: ");
            message = scanner.nextLine();
            outputStream.writeUTF(message);
            outputStream.flush();
            message = inputStream.readUTF();
            System.out.println("Server: " + message);
        }

        inputStream.close();
        outputStream.close();
        socket.close();
        scanner.close();
    }
}
