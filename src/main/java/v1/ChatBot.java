package v1;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.util.Span;

public class ChatBot implements Communicator {
    private List<Tokenizer> TokenList = new ArrayList<>() ;
    private TokenizerME tokenizer;
    private SentenceDetectorME sentenceDetector;
    private NameFinderME nameFinder;
    private String response;

    public ChatBot() throws IOException {
        //InputStream inputStream = new FileInputStream("C:\\Users\\j.nievelstein\\Java\\ChatAPI\\src\\main\\java\\en-token.bin");        // ist vorhanden
        //TokenizerModel tokenModel = new TokenizerModel(inputStream);
        //tokenizer = new TokenizerME(tokenModel);
        //// Initialize the sentence detector
        //SentenceModel sentModel = new SentenceModel(inputStream);
        //sentenceDetector = new SentenceDetectorME(sentModel);
        //// Initialize the name finder
        //inputStream = new FileInputStream("en-ner-person.bin");
        //TokenNameFinderModel nameModel = new TokenNameFinderModel(inputStream);
        //nameFinder = new NameFinderME(nameModel);;
        //inputStream.close();
    }
    public void handleInput(String input) throws IOException {
        String[] sentences = sentenceDetector.sentDetect(input);
        for (String sentence : sentences) {
            String[] tokens = tokenizer.tokenize(sentence);
            Span[] nameSpans = nameFinder.find(tokens);
            for (Span span : nameSpans) {
                for(int i = span.getStart(); i < span.getEnd(); i++){
                    System.out.println("Hello, " + tokens[i] + "!");
                    response = "Hello, " + tokens[i] + "!";
                }
            }
        }
        System.out.println(response);
    }
    private String processInput(String[] tokens) {
        // Code zur Verarbeitung der tokenisierten Benutzereingabe hier
        String input = "";
        for (String token : tokens) {
            input += token + " ";
        }
        input = input.trim();
        return input;
    }
    @Override
    public void communicate() {
        // Initialisieren des Tokenizers mit einem vorhandenen Modell
        //System.out.println(model);
        // Tokenisieren der Benutzereingabe
        //String[] tokens = tokenizer.tokenize(input);
        //System.out.println(Arrays.toString(tokens));
        // Code zur Verarbeitung der tokenisierten Benutzereingabe hier
    }
    public String getResponse() {
        return response;
    }


    static ServerSocket serverSocket;
    static PrintWriter out;
    static BufferedReader in;
    static OutputStream outputStream;
    public static void main(String[] args) {
        try {
            /*
             * create socket with port 8888
             * accept client socket
             * -> connect
             * starting communication
             * out for sending
             * in for retrieving
             */
            serverSocket = new ServerSocket(8888);
            System.out.println("Server gestartet, warte auf eingehende Verbindungen...");
            Socket clientSocket = serverSocket.accept();
            System.out.println("Verbindung hergestellt!");

            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out.println("Welcome to the chatbot!");



            InputStream inputStream = clientSocket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));


            outputStream = clientSocket.getOutputStream();
            // convert string to byte array
            byte[] message = "Hello World".getBytes();
            // write byte array to output stream
            outputStream.write(message);
            // flush output stream
            outputStream.flush();
            // close output stream and socket

            while (serverSocket.isBound()) {
                // scanner to scan the console for new input
                Scanner scanner = new Scanner(System.in);
                System.out.print("You: ");
                message = scanner.nextLine().getBytes();
                outputStream.write(message);
                outputStream.flush();

                message = inputStream.readAllBytes();
                System.out.println("Server: " + message);

                String receivedString = reader.readLine();
                System.out.println("Received string: " + receivedString);

                if (message.toString().equals("exit")) {
                    outputStream.close();
                    out.close();
                    in.close();
                    serverSocket.close();
                }
            }
        } catch (IOException e) {
            System.err.println("Fehler beim Starten des Servers: " + e.getMessage());
            try {
                outputStream.close();
                out.close();
                in.close();
                serverSocket.close();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }
}
