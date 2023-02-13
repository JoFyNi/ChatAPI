package v2;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {
    private static List<String> logs = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(8888);
        System.out.println("Server started on port 8888");
        while (true) {
            Socket socket = serverSocket.accept();
            System.out.println("Client connected");
            DataInputStream inputStream = new DataInputStream(socket.getInputStream());
            DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
            try {
                String message = "";
                while (!message.equals("exit chat")) {
                    message = inputStream.readUTF();
                    System.out.println("Client: " + message);
                    logs.add("Client: " + message);
                    outputStream.writeUTF(message);
                    outputStream.flush();
                    message = inputStream.readUTF();
                    System.out.println("Server: " + message);
                }
            } catch (IOException e) {
                System.out.println("Client disconnected");
            } finally {
                inputStream.close();
                outputStream.close();
                socket.close();
            }
        }
    }

    public static List<String> getLogs() {
        return logs;
    }
}
