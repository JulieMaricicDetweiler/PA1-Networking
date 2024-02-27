import java.io.*;
import java.net.*;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

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
                str = str.trim();
                System.out.println("Got request from client: \"" + str + "\"");
                if ("bye".equalsIgnoreCase(str)) {
                    toClient.println("disconnected");
                    break;
                } else if (str.matches("Joke 1|Joke 2|Joke 3")) {
                    int fileNumber = Integer.parseInt(str.substring(str.length() - 1));
                    sendJoke(toClient, fileNumber);
                } else {
                    toClient.println("Error: Bad Request. Please only enter one of the following options as your request: [Joke 1, Joke 2, Joke 3]");
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
            toClient.println("END OF JOKE");
            jokeReader.close();
        } catch (FileNotFoundException e) {
            toClient.println("Joke file not found.");
        } catch (IOException e) {
            toClient.println("Error reading joke file.");
        }
        toClient.flush();
    }
}
