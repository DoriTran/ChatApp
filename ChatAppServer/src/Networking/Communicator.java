package Networking;

import Data.*;
import Main.Main;

import java.io.*;
import java.net.Socket;

public class Communicator extends Thread {
    private ClientInfo client;

    public Communicator(Socket clientSocket) throws IOException {
        client = new ClientInfo(null,0, clientSocket);
    }

    @Override
    public void run() {
        try {
            while (true) {
                String header = client.receiver.readLine();
                if (header == null)
                    throw new IOException();

                System.out.println("Header: " + header);
                switch (header) {
                    case "user login": {
                        // Content: UserName
                        String loginUsername = client.receiver.readLine();

                        // Check Exists
                        boolean userNameExisted = false;
                        for (ClientInfo connected : SocketManager.clientInfos) {
                            if (connected.UserName.equals(loginUsername)) {
                                userNameExisted = true;
                                break;
                            }
                        }

                        // New User Login
                        if (!userNameExisted) {
                            // Update screen
                            client.UserName = loginUsername;
                            SocketManager.addNewClient(client);
                            Main.serverScreen.updateClientTable();

                            // client.sender Header
                            client.sender.write("user login success"); client.sender.newLine();
                            client.sender.flush();

                            // client.sender Content: User online total
                            client.sender.write("" + (SocketManager.clientInfos.size())); client.sender.newLine();
                            client.sender.flush();

                            // client.sender Content: User online names
                            for (ClientInfo other_client : SocketManager.clientInfos) {
                                if (other_client.UserName.equals(client.UserName))
                                    continue;
                                client.sender.write(other_client.UserName); client.sender.newLine();
                                client.sender.flush();
                            }

                            // Forecast all user
                            for (ClientInfo other_client : SocketManager.clientInfos) {
                                if (other_client.UserName.equals(client.UserName))
                                    continue;
                                other_client.sender.write("online user"); other_client.sender.newLine();
                                other_client.sender.write(client.UserName); other_client.sender.newLine();
                                other_client.sender.flush();
                            }
                        }
                        // Fail login
                        else {
                            client.sender.write("login failed"); client.sender.newLine();
                            client.sender.flush();
                        }
                        break;
                    }
                    case "send message": {
                        // Read From-To-Message data
                        String from = client.receiver.readLine();
                        String to = client.receiver.readLine();
                        String message = client.receiver.readLine();

                        // Send message to user
                        for (ClientInfo to_client : SocketManager.clientInfos) {
                            if (to_client.UserName.equals(to)) {
                                to_client.sender.write("new message"); to_client.sender.newLine();
                                to_client.sender.write(from); to_client.sender.newLine();
                                to_client.sender.write(message); to_client.sender.newLine();
                                to_client.sender.flush();
                            }
                        }

                        break;
                    }
                    case "get name": {
                        client.sender.write(SocketManager.serverInfo.serverName);
                        client.sender.newLine();
                        client.sender.flush();
                        break;
                    }
                    case "request download file": {

                        break;
                    }
                }
            }

        } catch (IOException e) {
            if (!SocketManager.serverSocket.isClosed() && client.UserName != null) {
                try {
                    SocketManager.clientInfos.remove(new ClientInfo(client.UserName));
                    Main.serverScreen.updateClientTable();

                    // Forecast all user
                    for (ClientInfo other_client : SocketManager.clientInfos) {
                        if (other_client.UserName.equals(client.UserName))
                            continue;

                        other_client.sender.write("offline user"); other_client.sender.newLine();
                        other_client.sender.write(client.UserName); other_client.sender.newLine();
                        other_client.sender.flush();
                    }

                    // Close offline user socket
                    client.socket.close();

                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                SocketManager.clientInfos.remove(client);
                Main.serverScreen.updateClientTable();
            }
        }
    }

}
