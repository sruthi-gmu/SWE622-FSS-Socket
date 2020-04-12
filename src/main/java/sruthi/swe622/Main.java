package sruthi.swe622;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        if((args[0].equals("server"))&& (args[1].equals("start"))&&(args[2].equals("8000"))){
            int portNumber =Integer.parseInt(args[2]);
            FileServer.start(portNumber);
        }
        else if(args[0].equals("client")) {
            if(args[1].equals("upload")) {
                String pathOnClient = args[2];
                String pathOnServer = args[3];
                Client.uploadFile(pathOnClient,pathOnServer);
            }
            else if(args[1].equals("download")) {
                String pathExistingFileNameOnServer = args[2];
                String pathOnClient = args[3];
                Client.downloadFile(pathExistingFileNameOnServer,pathOnClient);
            }
            else if(args[1].equals("dir")) {
                String pathOfexistingDirectoryOnServer = args[2];
                Client.listDirectory(pathOfexistingDirectoryOnServer);
            }
            else if(args[1].equals("mkdir")) {
                String pathOfNewDirectoryOnServer = args[2];
                Client.makeDirectory(pathOfNewDirectoryOnServer);
            }
            else if(args[1].equals("rmdir")) {
                String pathOfDirectoryToBeRemoved = args[2];
                Client.removeDirectory(pathOfDirectoryToBeRemoved);
            }
            else if(args[1].equals("rm")) {
                String pathOfFile = args[2];
                Client.removeFile(pathOfFile);
            }
            else if(args[1].equals("shutdown")) {
                Client.shutdown();
            }
            else
                System.out.println("Invalid Argument");
        }
        else
            System.out.println("Invalid Argument");

    }
}
