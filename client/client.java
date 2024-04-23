import java.io.*;
import java.net.*;
import java.util.Scanner;

public class ChatClient {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 1234;

    public static void main(String[] args) {
        try {
            Scanner scanner = new Scanner(System.in);
            Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
            System.out.println("Connected to the chat server");

            System.out.print("Enter your username: ");
            String userName = scanner.nextLine();

            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.println(userName); // Send username immediately after connecting

            new Thread(new ReceivedMessagesHandler(socket)).start();

            String userInput;
            System.out.println("Type a message (or 'exit' to quit):");
            while (!(userInput = scanner.nextLine()).equals("exit")) {
                out.println(userInput);
            }

            socket.close();
            scanner.close();
        } catch (IOException ex) {
            System.out.println("Error connecting to the server: " + ex.getMessage());
        }
    }

    static class ReceivedMessagesHandler implements Runnable {
        private Socket socket;
        private BufferedReader in;

        public ReceivedMessagesHandler(Socket socket) throws IOException {
            this.socket = socket;
            this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        }

        public void run() {
            try {
                String message;
                while ((message = in.readLine()) != null) {
                    System.out.println(message);
                }
            } catch (IOException e) {
                System.out.println("Error reading from server: " + e.getMessage());
            }
        }
    }
}
