package sruthi.swe622;

import java.io.*;
import java.net.Socket;

public class Client {

    public static void uploadFile(String pathOnClient,String pathOnServer) throws IOException {
        Socket clientSocket = createSocket();
        DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
        outToServer.writeBytes("upload");
        outToServer.writeBytes("=");
        outToServer.writeBytes(pathOnServer);
        outToServer.writeBytes(";");


        InputStream inFile = new FileInputStream(pathOnClient);
        int byteRead;
        while ((byteRead = inFile.read()) != -1) {
            outToServer.write(byteRead);
        }
        System.out.println("The file is successfully uploaded ");
    }

    public static void downloadFile(String pathOnServer, String pathOnClient) throws IOException {
        Socket clientSocket = createSocket();

        DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
        outToServer.writeBytes("download");
        outToServer.writeBytes("=");
        outToServer.writeBytes(pathOnServer);
        outToServer.writeBytes(";");

        InputStream inFromServer = new BufferedInputStream(clientSocket.getInputStream());
        OutputStream outToFile = new FileOutputStream(pathOnClient);

        int byteRead;
        StringBuilder resultBuilder = new StringBuilder();
        while ((byteRead = inFromServer.read()) != '=') {
            resultBuilder.append((char)byteRead);
        }
        String result = resultBuilder.toString();
        if(result.equals("Success")) {
            while ((byteRead = inFromServer.read()) != -1) {
                outToFile.write(byteRead);

            }
            System.out.println("The file is successfully downloaded ");
        }
        else {
            System.err.println("The file doesn't exist on the server");
            System.exit(-1);
        }

    }

    public static void listDirectory(String directoryPath)  throws IOException{
        Socket clientSocket = createSocket();
        DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
        outToServer.writeBytes("dir");
        outToServer.writeBytes("=");
        outToServer.writeBytes(directoryPath);
        outToServer.writeBytes(";");

        InputStream inFromServer = clientSocket.getInputStream();
        DataOutputStream out = new DataOutputStream(System.out);

        int byteRead;
        StringBuilder resultBuilder = new StringBuilder();
        while ((byteRead = inFromServer.read()) != '=') {
            resultBuilder.append((char) byteRead);
        }
        String result = resultBuilder.toString();
            if(result.equals("Success")) {
                while ((byteRead = inFromServer.read()) != -1) {
                    out.write(byteRead);
                }
            }
            else {
                System.err.println("Something went wrong");
                System.exit(-1);
            }
        }

    public static void makeDirectory(String newDirectory) throws IOException {
        Socket clientSocket = createSocket();
        DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
        outToServer.writeBytes("mkdir");
        outToServer.writeBytes("=");
        outToServer.writeBytes(newDirectory);
        outToServer.writeBytes(";");

        InputStream inFromServer = clientSocket.getInputStream();
        DataOutputStream out = new DataOutputStream(System.out);

        int byteRead;
        StringBuilder resultBuilder = new StringBuilder();
        while ((byteRead = inFromServer.read()) != -1) {
            resultBuilder.append((char) byteRead);
        }
        String result = resultBuilder.toString();
            if (result.equals("Success")) {
                System.out.println("The new directory is created");

            } else if(result.equals("Exist")) {
                System.out.println("The directory is already exists");

            }
            else {
                System.err.println("Something went wrong");
                System.exit(-1);
            }
        }

//        while ((byteRead = inFromServer.read()) != -1) {
//            out.write(byteRead);
//        }


    public static void removeDirectory(String directory) throws IOException{
        Socket clientSocket = createSocket();
        DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
        outToServer.writeBytes("rmdir");
        outToServer.writeBytes("=");
        outToServer.writeBytes(directory);
        outToServer.writeBytes(";");

        InputStream inFromServer = clientSocket.getInputStream();

        int byteRead;
        StringBuilder resultBuilder = new StringBuilder();
        while ((byteRead = inFromServer.read()) != -1) {
            resultBuilder.append((char) byteRead);
        }
        String result = resultBuilder.toString();
            if (result.equals("Success")) {
                System.out.println("The directory is deleted");

            } else if(result.equals("NonEmpty")) {
                System.out.println("The directory is not empty, cannot delete");

            }
            else {
                System.err.println("Something went wrong");
                System.exit(-1);
            }
        }

    public static void removeFile(String file) throws IOException{
        Socket clientSocket = createSocket();
        DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
        outToServer.writeBytes("rm");
        outToServer.writeBytes("=");
        outToServer.writeBytes(file);
        outToServer.writeBytes(";");

        InputStream inFromServer = clientSocket.getInputStream();
        DataOutputStream out = new DataOutputStream(System.out);

        int byteRead;
        StringBuilder resultBuilder = new StringBuilder();
        while ((byteRead = inFromServer.read()) != -1) {
            resultBuilder.append((char) byteRead);

        }
        String result = resultBuilder.toString();
            if (result.equals("Success")) {
                System.out.println("The file is deleted");

            }
            else {
                System.err.println("Something went wrong");
                System.exit(-1);
            }
        }


    public static void shutdown()throws IOException {
        Socket clientSocket = createSocket();
        DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
        outToServer.writeBytes("shutdown");
        outToServer.writeBytes("=");


    }

    private static Socket createSocket() throws IOException {
        String env = System.getenv("PA1_SERVER");
        String[] envArray = env.split(":");
        int portNumber = Integer.parseInt(envArray[1]);
        return new Socket(envArray[0], portNumber);
    }

}
