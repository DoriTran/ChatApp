package Networking;

import Data.ClientInfo;
import Data.ServerInfo;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.LinkedList;

public class SocketManager {
    static public ServerInfo serverInfo;
    static public LinkedList<ClientInfo> clientInfos;
    static public String serverIP;
    static public ServerSocket serverSocket;

    static public void initServerIP() {
        try {
            serverIP = InetAddress.getLocalHost().getHostAddress();
        }
        catch (UnknownHostException e) {
            System.out.println("Unknown host exception");
        }
    }

    static public void setNewActiveServer(ServerInfo newServer) {
        serverInfo = newServer;
        clientInfos.clear();
    }

    static public void addNewClient(ClientInfo clientInfo) {
        clientInfos.addLast(clientInfo);
    }

    static public void openServer() {
        try {
            serverSocket = new ServerSocket(serverInfo.serverPort);
            clientInfos = new LinkedList<ClientInfo>();

            new Thread(() -> {
                try {
                    do {
                        System.out.println("Waiting for client");

                        Socket clientSocket = serverSocket.accept();

                        Communicator clientCommunicator = new Communicator(clientSocket);
                        clientCommunicator.start();

                    } while (serverSocket != null && !serverSocket.isClosed());
                } catch (IOException e) {
                    System.out.println("Server or client socket closed");
                }
            }).start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static public void closeServer() {
        try {
            for (ClientInfo clientInfo : clientInfos)
                clientInfo.socket.close();
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
