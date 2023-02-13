package v1;

import java.io.*;
import java.net.Inet4Address;
import java.net.Socket;
import java.util.Scanner;

public class user implements Communicator{
    private final ChatBot chatBot = new ChatBot();
    public user() throws IOException {
        communicate();
    }

    static Socket socket;
    static OutputStream outputStream;

    public static void main(String[] args) throws IOException {
        /*
         * create socket with port 8888
         * accept client socket
         * -> connect
         * starting communication
         * out for sending
         * in for retrieving
         */
        try {
            Socket socket = new Socket("localhost", 8888);
            System.out.println("Verbindung zum Server hergestellt!");
            InputStream inputStream = socket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));


            outputStream = socket.getOutputStream();
            // convert string to byte array
            byte[] message = "Hello World".getBytes();
            // write byte array to output stream
            outputStream.write(message);
            // flush output stream
            outputStream.flush();
            // close output stream and socket

            while (socket.isConnected()) {
                // scanner to scan the console for new input
                Scanner scanner = new Scanner(System.in);
                message = scanner.nextLine().getBytes();
                outputStream.write(message);
                outputStream.flush();

                String receivedString = reader.readLine();
                System.out.println("Received string: " + receivedString);

                if (message.toString().equals("exit")) {
                    outputStream.close();
                    socket.close();
                }
            }
        } catch (IOException e) {
            System.err.println("Fehler beim Starten des Servers: " + e.getMessage());
            try {
                outputStream.close();
                socket.close();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
        //new user();
    }

    @Override
    public void communicate() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to ChatBot");
        String input = "";
        while(true) {
            System.out.println("Please enter your message");
            System.out.print("You: ");
            input = scanner.nextLine();
            if (input.equals("exit")) {
                break;
            }
            try {
                message.save(Inet4Address.getLocalHost().getHostName(), input);
                chatBot.handleInput(input);
                System.out.println("Bot: " + chatBot.getResponse());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        scanner.close();
    }
}