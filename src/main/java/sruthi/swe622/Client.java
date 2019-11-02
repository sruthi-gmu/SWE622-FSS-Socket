package sruthi.swe622;

import java.io.*;
import java.net.Socket;

public class Client {

    public static void uploadFile(String pathOnClient, String pathOnServer) throws IOException {
        Socket clientSocket = createSocket();
        DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
        outToServer.writeBytes("upload");
        outToServer.writeBytes("=");
        String positionFile = pathOnClient + ".pos";
        File positionFileName = new File(positionFile);
        int position = 0;

        try {
            if (positionFileName.exists()) {
                FileInputStream posStream = new FileInputStream(positionFileName);
                position = posStream.read() - 1;
            }
            if (position == 0) {
                outToServer.writeBytes("new");
                outToServer.writeBytes(";");

            } else {
                outToServer.writeBytes("continue");
                outToServer.writeBytes(";");
            }
            outToServer.writeBytes(pathOnServer);
            outToServer.writeBytes(";");

            RandomAccessFile inFile = new RandomAccessFile(pathOnClient, "r");
            RandomAccessFile out = new RandomAccessFile(positionFile, "rw");
            int byteRead, counter = 0;
            inFile.seek(position);
            while ((byteRead = inFile.read()) != -1) {
                outToServer.write(byteRead);
                counter++;
                out.seek(0);
                out.write(counter);

            }

            out.close();
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

        File positionFile = new File(pathOnClient + ".pos");
        long position = 0;
        if (positionFile.exists()) {
            BufferedReader posFileReader = new BufferedReader(new FileReader(positionFile));

            String posFromFile = posFileReader.readLine();
            System.out.println("posFromFile = [" + posFromFile + "]");

            position = Long.parseLong(posFromFile);
            System.out.println("Position to continue download from: " + position);
            posFileReader.close();
        }

        Boolean append;
        if (position == 0) {
            append = false;
        } else {
            append = true;
        }

        outToServer.writeBytes(String.valueOf(position));
        outToServer.writeBytes(";");
        outToServer.writeBytes(pathOnServer);
        outToServer.writeBytes(";");

        InputStream inFromServer = new BufferedInputStream(clientSocket.getInputStream());
        OutputStream outToFile = new FileOutputStream(pathOnClient, append);

        int byteRead;
        StringBuilder resultBuilder = new StringBuilder();
        while ((byteRead = inFromServer.read()) != '=') {
            resultBuilder.append((char) byteRead);
        }
        String result = resultBuilder.toString();

        if (result.equals("Success")) {
            int length;
            byte[] buffer = new byte[10 * 1024];
            while ((length = inFromServer.read(buffer)) > 0) {
                outToFile.write(buffer, 0, length);
                position += length;
                PrintWriter writer = new PrintWriter(positionFile);
                writer.print(String.valueOf(position));
                writer.close();
            }
            System.out.println("The file is successfully downloaded ");
            positionFile.delete();
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
        DataOutputStream out = new DataOutputStream(System.out);

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
        System.out.println(result);
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
