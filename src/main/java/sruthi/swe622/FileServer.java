package sruthi.swe622;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class FileServer {
    public static void start() throws IOException {
        String env = System.getenv("PA1_SERVER");
        String[] envArray = env.split(":");
        int portNumeber = Integer.parseInt(envArray[1]);
        ServerSocket serverSocket = new ServerSocket(portNumeber);

        while(true) {
            Socket socketConnection = serverSocket.accept();
            System.out.println("Client is connected");
            InputStream inFromClient = socketConnection.getInputStream();

            int byteRead;
            StringBuilder fileName = new StringBuilder();
            while ((byteRead = inFromClient.read()) != ';') {
                fileName.append((char)byteRead);
            }
            System.out.println(fileName.toString());
            OutputStream outToFile = new FileOutputStream(fileName.toString());
            while ((byteRead = inFromClient.read()) != -1) {
                outToFile.write(byteRead);
            }
        }
    }


}
