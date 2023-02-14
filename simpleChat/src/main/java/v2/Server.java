package v2;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class Server {
    private static List<String> logs = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        serverTest();
    }
    public static void serverTest() throws IOException {
        ServerSocket serverSocket = new ServerSocket(8888);
        System.out.println("Server started on port 8888");
        while (true) {
            Socket socket = serverSocket.accept();
            System.out.println("Client connected");
            DataInputStream inputStream = new DataInputStream(socket.getInputStream());
            DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
            getMessage(inputStream);
            try {
                Scanner scanner = new Scanner(System.in);
                String message = "";
                while (!message.equals("exit chat")) {

                    System.out.print("You: ");
                    message = scanner.nextLine();
                    outputStream.writeUTF(message);
                    outputStream.flush();
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                //message = inputStream.readUTF();
                //System.out.println("Client: " + message);
                //logs.add("Client: " + message);
            } catch (IOException e) {
                System.out.println("Client disconnected");
            } finally {
                inputStream.close();
                outputStream.close();
                socket.close();
            }
        }
    }
    public static void getMessage(DataInputStream inputStream) throws IOException {
        java.util.Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            public void run() {
                String message = "";
                try {
                    message = inputStream.readUTF();
                    System.out.println("Client: " + message);
                    logs.add("Client: " + message);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        };
        timer.schedule(task, 0, 100);
    }

    public static List<String> getLogs() {
        return logs;
    }
}
