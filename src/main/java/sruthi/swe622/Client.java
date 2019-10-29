package sruthi.swe622;

import java.io.*;
import java.net.Socket;

public class Client {
    public static void sendFile(String file) throws IOException {
        String env = System.getenv("PA1_SERVER");
        String[] envArray = env.split(":");
        int portNumeber = Integer.parseInt(envArray[1]);
        Socket clientSocket = new Socket(envArray[0],portNumeber);
        String[] fileNameArray = file.split("/");
        String fileName = fileNameArray[fileNameArray.length-1];
        DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
        outToServer.writeBytes(fileName);
        outToServer.writeBytes(";");
        FileInputStream inFile = new FileInputStream(file);
        int byteRead;
        while ((byteRead = inFile.read()) != -1) {
            outToServer.write(byteRead);
        }
    }
    public static void downloadFile(String file) throws IOException {
        String env = System.getenv("PA1_SERVER");
        String[] envArray = env.split(":");
        int portNumeber = Integer.parseInt(envArray[1]);
        Socket clientSocket = new Socket(envArray[0],portNumeber);


    }
}
