package Data;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class ClientInfo {
    public String UserName;
    public Integer ServerPort;

    public Socket socket;
    public BufferedReader receiver;
    public BufferedWriter sender;

    public ClientInfo(String userName) {
        this.UserName = userName;
    }
    public ClientInfo(String userName, Integer serverPort, Socket socket) {
        try {
            this.UserName = userName;
            this.ServerPort = serverPort;
            this.socket = socket;
            this.receiver = new BufferedReader( new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
            this.sender = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean equals(Object object) {
        ClientInfo clientInfo = (ClientInfo) object;
        return (clientInfo.UserName.equals(this.UserName));
    }
}
