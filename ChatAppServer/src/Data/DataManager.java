package Data;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;

public class DataManager {
    public static LinkedList<ServerInfo> serverInfos = new LinkedList<>();

    public static Object[][] getObjectsData() {
        // Null server info
        if (serverInfos == null)
            return new Object[][] {};

        // Create Object Data
        Object[][] objects = new Object[serverInfos.size()][5];

        for (int i = 0; i < serverInfos.size(); i++) {
            objects[i] = serverInfos.get(i).getDataObject();
        }

        return objects;
    }

    // File handle
    public static void loadSaves() throws IOException {
        // Load data from file
        BufferedReader bufferedReader = new BufferedReader(new FileReader("ServerData.txt"));

        String serverData;
        while ((serverData = bufferedReader.readLine()) != null) {
            String[] server = serverData.split(",");

            serverInfos.addLast(new ServerInfo(server[0], Integer.parseInt(server[1])));
        }
        bufferedReader.close();

    }
    public static void uploadSaves() throws IOException {
        FileWriter writer = new FileWriter("ServerData.txt");

        // Write data to file
        for (int index = 0; index < serverInfos.size(); index++) {
            writer.write(serverInfos.get(index).serverName + "," + serverInfos.get(index).serverPort);
            if (index != serverInfos.size() -1) writer.write("\n");
        }

        writer.close();
    }

    // Data handle
    public static void switchStatusServer(int at) {
        // Swap at AT
        serverInfos.get(at).serverStatus = !serverInfos.get(at).serverStatus;
        // If not at AT all false
        for (int i = 0; i < serverInfos.size(); i++) {
            if (i == at) continue;
            serverInfos.get(i).serverStatus = false;
        }
    }
    public static void addServer(ServerInfo serverInfo) {
        serverInfos.addLast(serverInfo);
    }
    public static void removeServer(Integer at) {
        serverInfos.remove(at.intValue());
    }

    // Data interact
    public static boolean isExist(ServerInfo serverInfo) {
        return serverInfos.contains(serverInfo);
    }
}
