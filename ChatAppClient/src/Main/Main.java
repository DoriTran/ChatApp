package Main;

import Networking.SocketManager;

public class Main {
    // Screen
    public static ServerScreen serverScreen;
    public static ChatScreen chatScreen;

    // Networking
    public static SocketManager socketManager;

    // Main
    public static void main(String[] args) {
        serverScreen = new ServerScreen();
    }
}