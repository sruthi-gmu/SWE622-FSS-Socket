package sruthi.swe622;

import java.io.*;
import java.net.Socket;

public class FileServerAgent extends Thread {
    Socket socketConnection;

    public FileServerAgent(Socket clientSocket) {
        socketConnection = clientSocket;
    }

    @Override
    public void run() {
        try {
            processRequest();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void processRequest() throws IOException {

//        String fssRoot = System.getenv("FSS_ROOT");

        System.out.println("Client is connected");
        InputStream inFromClient = socketConnection.getInputStream();
        OutputStream outToClient = socketConnection.getOutputStream();

        int byteRead;
        StringBuilder optionBuilder = new StringBuilder();
        while ((byteRead = inFromClient.read()) != '=') {
            optionBuilder.append((char) byteRead);
        }
        String option = optionBuilder.toString();

        if (option.equals("upload")) {
            Boolean append = true;
            StringBuilder status = new StringBuilder();
            while ((byteRead = inFromClient.read()) != ';') {
                status.append((char) byteRead);
            }
            System.out.println(status);
            if(status.toString().equals("new")){
                append = false;
            }
            StringBuilder fileName = new StringBuilder();
            while ((byteRead = inFromClient.read()) != ';') {
                fileName.append((char) byteRead);
            }
            System.out.println(append);

            OutputStream outToFile = new FileOutputStream(fileName.toString(),append);
            while ((byteRead = inFromClient.read()) != -1) {
                outToFile.write(byteRead);
            }
            outToClient.flush();
            outToClient.close();

        } else if (option.equals("download")) {
            StringBuilder positionBuider = new StringBuilder();
            while ((byteRead = inFromClient.read()) != ';') {
                positionBuider.append((char) byteRead);
            }
            long position = Long.parseLong(positionBuider.toString());
            System.out.println("Send to client from position: " + position);

            StringBuilder fileName = new StringBuilder();
            while ((byteRead = inFromClient.read()) != ';') {
                fileName.append((char) byteRead);
            }
            try {
                outToClient.write("Success".getBytes());
                outToClient.write("=".getBytes());
                RandomAccessFile inFile = new RandomAccessFile(fileName.toString(),"r");
                inFile.seek(position);
                while ((byteRead = inFile.read()) != -1) {
                    outToClient.write(byteRead);
                }

            } catch (Exception e) {
                System.out.println(e.getMessage());
                outToClient.write("Failure".getBytes());
                outToClient.write("=".getBytes());
            }
            outToClient.flush();
            outToClient.close();
        } else if (option.equals("dir")) {
            StringBuilder directoryName = new StringBuilder();
            while ((byteRead = inFromClient.read()) != ';') {
                directoryName.append((char) byteRead);
            }
            try {
                File dir = new File(directoryName.toString());
                if (dir.exists()) {
                    outToClient.write("Success".getBytes());
                    outToClient.write("=".getBytes());
                    File[] list = new File(directoryName.toString()).listFiles();
                    for (int i = 0; i < list.length; i++) {
                        outToClient.write(i);
                        String file = list[i].toString();
                        outToClient.write(file.getBytes());
                        outToClient.write("\n".getBytes());
                    }
                }
            } catch (Exception e) {
                outToClient.write("Failure".getBytes());
                outToClient.write("=".getBytes());
            }
            outToClient.flush();
            outToClient.close();
        } else if (option.equals("mkdir")) {
            StringBuilder directoryName = new StringBuilder();
            while ((byteRead = inFromClient.read()) != ';') {
                directoryName.append((char) byteRead);
            }
            try {
                File newDir = new File(directoryName.toString());
                if (!newDir.exists()) {
                    outToClient.write("Success".getBytes());
                    newDir.mkdir();
                } else {
                    outToClient.write("Exist".getBytes());
                }

            } catch (Exception e) {
                outToClient.write("Failure".getBytes());

            }
            outToClient.flush();
            outToClient.close();
        } else if (option.equals("rmdir")) {
            StringBuilder directoryName = new StringBuilder();
            while ((byteRead = inFromClient.read()) != ';') {
                directoryName.append((char) byteRead);
            }
            try {
                File dir = new File(directoryName.toString());
                System.out.println("The file is " + dir);
                if (dir.exists()) {
                    String[] dirlist = dir.list();
                    if (dirlist.length == 0) {
                        outToClient.write("Success".getBytes());
                        dir.delete();
                    }
                    else {
                        outToClient.write("NonEmpty".getBytes());
                    }
                }

            } catch (Exception e) {
                outToClient.write("Failure".getBytes());

            }
            outToClient.flush();
            outToClient.close();
        } else if (option.equals("rm")) {
            StringBuilder fileName = new StringBuilder();
            while ((byteRead = inFromClient.read()) != ';') {
                fileName.append((char) byteRead);
            }
            try {
                File file = new File(fileName.toString());
                if (file.exists()) {
                    outToClient.write("Success".getBytes());
                    file.delete();
                }

            } catch (Exception e) {
                outToClient.write("Failure".getBytes());

            }
            outToClient.flush();
            outToClient.close();
        } else if (option.equals("shutdown")) {
            outToClient.write("shutdown".getBytes());
            outToClient.flush();
            outToClient.close();
            System.exit(0);
        } else {
            System.out.println("error");
        }

    }


}
