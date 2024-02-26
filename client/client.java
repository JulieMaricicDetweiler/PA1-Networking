import java.io.*;
import java.net.*;
import java.util.Scanner;


public class client {
    public static void main(String[] args) {
        // Check if the correct number of arguments are passed (Server IP and the Port Number)
        if (args.length != 2) {
            System.out.println("Usage: java Client <Server IP> <Port Number>");
            return;
        }

        String serverIP = args[0]; //Storing the server IP
        int port = Integer.parseInt(args[1]); //Parsing and storing the Port Number


        try (Socket socket = new Socket(serverIP, port); //Establishing Connection with server
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true); //To send data to the server
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream())); //To receive data from the server
             Scanner scanner = new Scanner(System.in)) { //Scanner to read user input

            // Initial server response (greeting)
            System.out.println("Server says: " + in.readLine());

            // Interaction with the server until the user says bye
            String inputLine;
            while (true) {
                System.out.print("Enter command (Joke 1/Joke 2/Joke 3/bye): ");
                inputLine = scanner.nextLine();
                if ("bye".equalsIgnoreCase(inputLine)) { //if command is bye then terminate
                    out.println(inputLine); //Sending bye to server
                    System.out.println("Exiting.");
                    break;
                }
                out.println(inputLine); //sending user command to server
                System.out.println("Server response: " + in.readLine());
            }

        } catch (IOException e) { //catching exceptions and printing an error message
            System.out.println("Client Error: " + e.getMessage());
        }
    }
}
