import java.io.*;
import java.net.*;



public class Server {
    public static void main(String[] args) {
        //validate that command is inputted in proper form (java server [port_number])

        //get port number from arguments

        //create a socket with port number
        //infinitely:
            //accept incomping connections
            //processRequest()
    
    }

    private static void processRequest(Socket client) {
        //send "Hello!" to client

        //while client request line is not empty
        //parse client request to get number of the file they query
            //if client says bye:
                //terminate connection
            //else:
                //if joke query is in correct form (Joke 1 || Joke 2 || Joke 3):
                    //parse request to get file number
                    //sendJoke()
                //else:
                    //error
        
        //close connection

    }

    private static void sendJoke(PrinterWriter out, int fileNum) {

        //read joke from correct file using java fileIO
        //print joke

    }
}