package com.example.tcpserver;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class TCP {
    public static void main(String[] args) throws Exception {

        ServerSocket welcomeSocket = new ServerSocket(6969);

        while (true) {
            Socket connectionSocket = welcomeSocket.accept();
            BufferedReader inFromClient =
                    new BufferedReader(new
                            InputStreamReader(connectionSocket.getInputStream()));
            DataOutputStream outToClient =
                    new DataOutputStream(connectionSocket.getOutputStream());
            String user = inFromClient.readLine();

            if (user.split("GG")[0].split("@")[1].equals("login")) {
                if (Persistent.loginUser(user
                        .split("GG")[0].split("@")[0].split("-")[0], user.split("GG")[0].split("@")[0].split("-")[1])) {
                    Persistent.addActive(user
                            .split("GG")[1] + "==" + user
                            .split("GG")[0].split("@")[0].split("-")[0]);
                    outToClient.writeBytes("success" + "\n");
                    connectionSocket.close();


                } else {
                    outToClient.writeBytes("fail" + "\n");
                    connectionSocket.close();


                }
            } else {
                if (user.split("@")[1].equals("remove")) {
                    System.out.println(user);
                    Persistent.removeUserFromActive(user.split("@")[0]);
                    outToClient.writeBytes("done" + "\n");
                    connectionSocket.close();


                } else {
                    if (user.equals("active@active")) {
                        String active = Persistent.fetchActive();
                        System.out.println(active);
                        outToClient.writeBytes(active + "\n");
                        connectionSocket.close();
                    }

                }

            }

        }

    }

}

