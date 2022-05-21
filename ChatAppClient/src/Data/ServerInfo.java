package Data;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;

public class ServerInfo {
    public String serverName;
    public String serverIP;
    public Integer serverPort;
    public Boolean serverStatus;

    public ServerInfo(String serverName, String serverIP, Integer serverPort) {
        this.serverName = serverName;
        this.serverIP = serverIP;
        this.serverPort = serverPort;
        this.serverStatus = false;
    }

    public ServerInfo(String serverName, String serverIP, Integer serverPort, Boolean serverStatus) {
        this.serverName = serverName;
        this.serverIP = serverIP;
        this.serverPort = serverPort;
        this.serverStatus = serverStatus;
    }

    public Object[] getDataObject() {
        Object[] object = new Object[7];
        object[0] = serverName;
        object[1] = serverIP;
        object[2] = serverPort;
        object[3] = (serverStatus) ? "Đang hoạt động" : "Không hoạt động";
        object[4] = "Join";
        object[5] = "Edit";
        object[6] = "Delete";

        return object;
    }

    @Override
    public boolean equals(Object object) {
        ServerInfo server = (ServerInfo) object;
        return (server.serverIP.equals(this.serverIP) && server.serverPort.equals(this.serverPort));
    }

    public static boolean isServerOnline(String ip, int port) {
        try {
            Socket s = new Socket();
            s.connect(new InetSocketAddress(ip, port), 300);
            s.close();
            return true;
        } catch (IOException ex) {
            return false;
        }
    }

    public static String getServerName(String ip, int port) {

        if (!isServerOnline(ip, port))
            return "";

        try {
            Socket socket = new Socket(ip, port);
            BufferedReader receiver = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedWriter sender = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            sender.write("get name");
            sender.newLine();
            sender.flush();

            String name = receiver.readLine();

            socket.close();
            return name;
        } catch (IOException ex) {
            return "";
        }
    }
}
