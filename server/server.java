import java.io.*;
import java.net.*;



public class Server {
    public static void main(String[] args) {
        //validate that command is inputted in proper form (java server [port_number])
        String portRegex = "^([1-9][0-9]{0,3}|[1-5][0-9]{4}|6[0-4][0-9]{3}|65[0-4][0-9]{2}|655[0-2][0-9]|6553[0-5])$";
        if(args.length != 1 || !args[0].matches(portRegex)) {
            System.out.println("Please follow the format: 'java server [port_number]' with port number between 1 and 65535");
            return;
        }

        //get port number from arguments
        int port_number = Integer.parseInt(args[0]);

        //create a socket with port number
        ServerSocket server = new ServerSocket(port_number);
        //infinitely:
        while(true) {
            //accept incomping connections
            Socket client = server.accept();
            System.out.println("Got connection request from: " + client.getInetAddress()); //print client information
            //processRequest()
            processRequest(client);
        }

        server.close();
    
    }

    private static void processRequest(Socket client) {
        PrintWriter toClient = new PrintWriter(client.getOutputStream()); //create PS to write to client
        BufferedReader fromClient = new BufferedReader(new InputStreamReader(client.getInputStream())); //create BR to read from client socket
        //send "Hello!" to client
        toClient.println("Hello!");
        

        //while client request line is not empty
        String str;
        while((str = fromClient.readLine()) != null){
            System.out.println("Got request from client: \"" + str + "\"");
            //if client says bye
            if(str == "bye") {
                //terminate connection
                toClient.println("disconnected");
            }
            else {
                //if joke query is in not correct form (Joke 1 || Joke 2 || Joke 3), error
                String jokePattern = "Joke [1-3]";
                if(!str.matches(jokePattern)) {
                    toClient.println("Error: Bad Request. Please only enter one of the following options as your request: [Joke 1, Joke 2, Joke 3]");
                }
                else {
                    //parse request to get file number
                    int fileNumber = Integer.parseInt(String.valueOf(str.charAt(str.length() - 1)));
                    sendJoke(toClient, fileNumber);
                }
            }
        }
        
        //close connection
        System.out.println("Closing connection...");
        toClient.close();
        fromClient.close();
        client.close();
    }

    private static void sendJoke(PrintWriter toClient, int fileNum) {
        FileReader fileReader = new FileReader("joke" + fileNum + ".txt");
        BufferedReader jokeReader = new BufferedReader(fileReader);

        //read joke from correct file using java fileIO
        String joke;
        while((joke = jokeReader.readLine()) != null) {
            //write to client
            toClient.println(joke);
        }
    }
}