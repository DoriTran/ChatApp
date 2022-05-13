package Data;

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

    public static ArrayList<ServerInfo> getTestServerInfo() {
        ArrayList<ServerInfo> testData = new ArrayList<>();

        testData.add(new ServerInfo("Server A", "localhost", 1000, true));
        testData.add(new ServerInfo("Server B", "localhost", 1001, true));
        testData.add(new ServerInfo("Server C", "localhost", 1002, false));
        testData.add(new ServerInfo("Server D", "localhost", 1003, true));
        testData.add(new ServerInfo("Server E", "localhost", 1004, true));
        testData.add(new ServerInfo("Server F", "localhost", 1005, false));
        testData.add(new ServerInfo("Server G", "localhost", 1006, true));
        testData.add(new ServerInfo("Server H", "localhost", 1007, false));
        testData.add(new ServerInfo("Server I", "localhost", 1008, true));

        return testData;
    }
}
