package sruthi.swe622;

import java.io.*;
import java.net.Socket;

public class Client {

    public static void uploadFile(String pathOnClient, String pathOnServer) throws IOException {
        Socket clientSocket = createSocket();
        DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
        outToServer.writeBytes("upload");
        outToServer.writeBytes("=");
        File clientFile = new File(pathOnClient);
        long clientFileLength = clientFile.length();

        try {
            outToServer.writeBytes(String.valueOf(clientFileLength));
            outToServer.writeBytes(";");
            outToServer.writeBytes(pathOnServer);
            outToServer.writeBytes(";");

            InputStream inFromServer = new BufferedInputStream(clientSocket.getInputStream());
            int byteRead;
            StringBuilder serverFileLengthBuilder = new StringBuilder();
            while ((byteRead = inFromServer.read()) != ';') {
                serverFileLengthBuilder.append((char) byteRead);
            }
            int serverFileLength = Integer.parseInt(serverFileLengthBuilder.toString());

            RandomAccessFile inFile = new RandomAccessFile(pathOnClient, "r");
            inFile.seek(serverFileLength);
            int length;
            byte[] buffer = new byte[10 * 1024];
            while ((length = inFile.read(buffer)) > 0) {
                outToServer.write(buffer, 0, length);
                serverFileLength += length;
                float v = (float) serverFileLength / clientFileLength;
                long percentage = (long) (v * 100);
                System.out.print("\rUploading... " + percentage + "%");
            }
            System.out.println();
            inFile.close();
            outToServer.close();
            System.out.println("The file is successfully uploaded ");

        } catch (Exception e) {
            System.out.println("The file path on client does not exist");

        }
    }

    public static void downloadFile(String pathOnServer, String pathOnClient) throws IOException {
        Socket clientSocket = createSocket();

        DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
        outToServer.writeBytes("download");
        outToServer.writeBytes("=");

        long clientFileLength = new File(pathOnClient).length();
        outToServer.writeBytes(String.valueOf(clientFileLength));
        outToServer.writeBytes(";");
        outToServer.writeBytes(pathOnServer);
        outToServer.writeBytes(";");

        InputStream inFromServer = new BufferedInputStream(clientSocket.getInputStream());
        int byteRead;
        StringBuilder serverFileLengthBuilder = new StringBuilder();
        while ((byteRead = inFromServer.read()) != ';') {
            serverFileLengthBuilder.append((char) byteRead);
        }
        int serverFileLength = Integer.parseInt(serverFileLengthBuilder.toString());

        boolean append = false;
        if (clientFileLength < serverFileLength) {
            append = true;
        }

        StringBuilder resultBuilder = new StringBuilder();
        while ((byteRead = inFromServer.read()) != '=') {
            resultBuilder.append((char) byteRead);
        }
        String result = resultBuilder.toString();

        if (result.equals("Success")) {
            OutputStream outToFile = new FileOutputStream(pathOnClient, append);
            int length;
            byte[] buffer = new byte[10 * 1024];
            while ((length = inFromServer.read(buffer)) > 0) {
                outToFile.write(buffer, 0, length);
                clientFileLength += length;

                float v = (float) clientFileLength / serverFileLength;
                long percentage = (long) (v * 100);
                System.out.print("\rDownloading... " + percentage + "%");
            }
            System.out.println();
            System.out.println("The file is successfully downloaded ");
            outToFile.close();
        } else {
            System.err.println("The file doesn't exist on the server");
            System.exit(-1);
        }

    }

    public static void listDirectory(String directoryPath) throws IOException {
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
        if (result.equals("Success")) {
            while ((byteRead = inFromServer.read()) != -1) {
                out.write(byteRead);
            }
        } else {
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

        int byteRead;
        StringBuilder resultBuilder = new StringBuilder();
        while ((byteRead = inFromServer.read()) != -1) {
            resultBuilder.append((char) byteRead);
        }
        String result = resultBuilder.toString();
        if (result.equals("Success")) {
            System.out.println("The new directory is created");

        } else if (result.equals("Exist")) {
            System.out.println("The directory is already exists");

        } else {
            System.err.println("Something went wrong");
            System.exit(-1);
        }
    }

    public static void removeDirectory(String directory) throws IOException {
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

        } else if (result.equals("NonEmpty")) {
            System.out.println("The directory is not empty, cannot delete");

        } else {
            System.err.println("The directory does not exist");
            System.exit(-1);
        }
    }

    public static void removeFile(String file) throws IOException {
        Socket clientSocket = createSocket();
        DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
        outToServer.writeBytes("rm");
        outToServer.writeBytes("=");
        outToServer.writeBytes(file);
        outToServer.writeBytes(";");

        InputStream inFromServer = clientSocket.getInputStream();

        int byteRead;
        StringBuilder resultBuilder = new StringBuilder();
        while ((byteRead = inFromServer.read()) != -1) {
            resultBuilder.append((char) byteRead);

        }
        String result = resultBuilder.toString();
        if (result.equals("Success")) {
            System.out.println("The file is deleted");

        } else {
            System.err.println("File does not exist");
            System.exit(-1);
        }
    }

    public static void shutdown() throws IOException {
        Socket clientSocket = createSocket();
        DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
        outToServer.writeBytes("shutdown");
        outToServer.writeBytes("=");
        InputStream inFromServer = clientSocket.getInputStream();

        int byteRead;
        StringBuilder resultBuilder = new StringBuilder();
        while ((byteRead = inFromServer.read()) != -1) {
            resultBuilder.append((char) byteRead);

        }
        String result = resultBuilder.toString();
        if (result.equals("shutdown")) {
            System.out.println("The file server is shutdown");

        }
    }

    private static Socket createSocket() throws IOException {
        String env = System.getenv("PA1_SERVER");
        String[] envArray = env.split(":");
        int portNumber = Integer.parseInt(envArray[1]);
        return new Socket(envArray[0], portNumber);
    }

}
