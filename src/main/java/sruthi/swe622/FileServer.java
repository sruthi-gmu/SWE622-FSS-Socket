package sruthi.swe622;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class FileServer {
    public static void start(int portNumber) throws IOException {
        ServerSocket serverSocket = new ServerSocket(portNumber);
        while(true) {
            Socket socketConnection = serverSocket.accept();
            FileServerAgent newConnection = new FileServerAgent(socketConnection);
            newConnection.start();
        }

    }
}
