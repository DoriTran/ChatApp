package Data;

import java.net.Socket;

public class ClientInfo {
    public String UserName;
    public Integer ServerPort;
    public Socket socket;

    public ClientInfo(String userName, Integer serverPort, Socket socket) {
        this.UserName = userName;
        this.ServerPort = serverPort;
        this.socket = socket;
    }


}
