package v2;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

public class Client {
    public static void main(String[] args) throws IOException {
        ClientTest();
    }

    public static void ClientTest() throws IOException {
        Socket socket = new Socket("localhost", 8888);
        System.out.println("Connected to server");
        DataInputStream inputStream = new DataInputStream(socket.getInputStream());
        DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
        getMessage(inputStream);
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
        inputStream.close();
        outputStream.close();
        socket.close();
        scanner.close();
    }

    public static void getMessage(DataInputStream inputStream) throws IOException {
        java.util.Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            public void run() {
                String message = "";
                try {
                    message = inputStream.readUTF();
                    System.out.println("Server: " + message);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        };
        timer.schedule(task, 0, 100);
    }
}
