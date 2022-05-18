package Networking;

import Data.*;
import Main.Main;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class Communicator extends Thread {
    private ClientInfo client;
    public BufferedReader receiver;
    public BufferedWriter sender;

    public Communicator(Socket clientSocket) throws IOException {
        client = new ClientInfo("",0, clientSocket);
        receiver = new BufferedReader( new InputStreamReader(clientSocket.getInputStream(), StandardCharsets.UTF_8) );
        sender = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream(), StandardCharsets.UTF_8));
    }

    @Override
    public void run() {
        try {
            while (true) {
                String header = receiver.readLine();
                if (header == null)
                    throw new IOException();

                System.out.println("Header: " + header);
                switch (header) {

                    case "new login": {

                        String loginUsername = receiver.readLine();

                        boolean userNameExisted = false;
                        for (ClientInfo connected : SocketManager.clientInfos) {
                            if (connected.UserName.equals(loginUsername)) {
                                userNameExisted = true;
                                break;
                            }
                        }

                        if (!userNameExisted) {
                            client.UserName = loginUsername;
                            SocketManager.addNewClient(client);
                            Main.serverScreen.updateClientTable();

                            sender.write("login success");
                            sender.newLine();
                            sender.flush();

                            sender.write("" + (SocketManager.clientInfos.size() - 1));
                            sender.newLine();
                            sender.flush();
                            for (ClientInfo client : SocketManager.clientInfos) {
                                if (client.UserName.equals(client.UserName))
                                    continue;
                                sender.write(client.UserName);
                                sender.newLine();
                                sender.flush();
                            }

                            for (ClientInfo client : SocketManager.clientInfos) {
                                if (client.UserName.equals(client.UserName))
                                    continue;
                                sender.write("new user online");
                                sender.newLine();
                                sender.write(client.UserName);
                                sender.newLine();
                                sender.flush();
                            }
                        } else {
                            sender.write("login failed");
                            sender.newLine();
                            sender.flush();
                        }
                        break;
                    }

                    case "get name": {
                        sender.write(SocketManager.serverInfo.serverName);
                        sender.newLine();
                        sender.flush();
                        break;
                    }



                    case "request download file": {

                        break;
                    }


                }
            }

        } catch (IOException e) {

        }
    }

}
