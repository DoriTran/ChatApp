package Data;

import java.util.ArrayList;

public class ServerInfo {
    public String serverName;
    public Integer serverPort;
    public Boolean serverStatus;

    public ServerInfo(String serverName, Integer serverPort) {
        this.serverName = serverName;
        this.serverPort = serverPort;
        this.serverStatus = false;
    }

    public ServerInfo(String serverName, Integer serverPort, Boolean serverStatus) {
        this.serverName = serverName;
        this.serverPort = serverPort;
        this.serverStatus = serverStatus;
    }

    public Object[] getDataObject() {
        Object[] object = new Object[5];
        object[0] = serverName;
        object[1] = serverPort;
        object[2] = (serverStatus) ? "Đang hoạt động" : "Không hoạt động";
        object[3] = "Bật/Tắt";
        object[4] = "Xóa";

        return object;
    }

    @Override
    public boolean equals(Object object) {
        ServerInfo server = (ServerInfo) object;
        return (server.serverPort.equals(this.serverPort));
    }
}
