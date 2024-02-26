import java.io.*;
import java.net.*;

public class server {
    public static void main(String[] args) {
        int port_number = 0;
        ServerSocket server = null;
        try {
            // Validate port number and create server socket
            String portRegex = "^([1-9][0-9]{0,3}|[1-5][0-9]{4}|6[0-4][0-9]{3}|65[0-4][0-9]{2}|655[0-2][0-9]|6553[0-5])$";
            if (args.length != 1 || !args[0].matches(portRegex)) {
                System.out.println("Please follow the format: 'java server [port_number]' with port number between 1 and 65535");
                return;
            }
            port_number = Integer.parseInt(args[0]);
            server = new ServerSocket(port_number);
            System.out.println("Server is running on port " + port_number);

            // Accept connections infinitely
            while (true) {
                Socket client = server.accept();
                System.out.println("Got connection request from: " + client.getInetAddress());
                processRequest(client);
            }
        } catch (IOException e) {
            System.out.println("Server Exception: " + e.getMessage());
        } finally {
            if (server != null) {
                try {
                    server.close();
                } catch (IOException e) {
                    System.out.println("Error closing server: " + e.getMessage());
                }
            }
        }
    }

    private static void processRequest(Socket client) {
        try {
            PrintWriter toClient = new PrintWriter(client.getOutputStream(), true);
            BufferedReader fromClient = new BufferedReader(new InputStreamReader(client.getInputStream()));
            toClient.println("Hello!");

            String str;
            while ((str = fromClient.readLine()) != null) {
                System.out.println("Got request from client: \"" + str + "\"");
                if ("bye".equals(str)) {
                    toClient.println("disconnected");
                    break;
                } else {
                    String jokePattern = "Joke [1-3]";
                    if (!str.matches(jokePattern)) {
                        toClient.println("Error: Bad Request. Please only enter one of the following options as your request: [Joke 1, Joke 2, Joke 3]");
                    } else {
                        int fileNumber = Integer.parseInt(String.valueOf(str.charAt(str.length() - 1)));
                        sendJoke(toClient, fileNumber);
                    }
                }
            }
            System.out.println("Closing connection...");
            client.close();
        } catch (IOException e) {
            System.out.println("Error processing request: " + e.getMessage());
        }
    }

    private static void sendJoke(PrintWriter toClient, int fileNum) {
        try {
            FileReader fileReader = new FileReader("joke" + fileNum + ".txt");
            BufferedReader jokeReader = new BufferedReader(fileReader);
            String joke;
            while ((joke = jokeReader.readLine()) != null) {
                toClient.println(joke);
            }
            jokeReader.close();
        } catch (FileNotFoundException e) {
            toClient.println("Joke file not found.");
        } catch (IOException e) {
            toClient.println("Error reading joke file.");
        }
    }
}
