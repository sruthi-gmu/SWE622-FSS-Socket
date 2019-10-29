package sruthi.swe622;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        if(args[0].equals("--server")){
            FileServer.start();
        }
        else if(args[0].equals("--client")){
            if(args[1].equals("send")) {
                for (int i = 2; i < args.length; i++) {
                    Client.sendFile(args[i]);
                }
            }
            else if(args[1].equals("download")) {
                Client.downloadFile(args[2]);

            }
        }
        else
            System.out.println("Invalid Argument");

    }
}
